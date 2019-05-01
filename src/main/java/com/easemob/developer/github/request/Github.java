package com.easemob.developer.github.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rx.Observable;
import rx.functions.Func1;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author stliu at apache.org
 * @since 6/5/15
 */
@Component
@Slf4j
public class Github {
    public static final String GITHUB_ROOT = "https://api.github.com";
    public static final String GITHUB_V3_MEDIA_TYPE = "application/vnd.github.v3+json";
    public static final UriBuilder ROOT = UriBuilder.fromUri(GITHUB_ROOT);
    public static final String RATE_LIMIT_REMAINING_HEADER = "X-RateLimit-Remaining";
    public static final String RATE_LIMIT_RESET_HEADER = "X-RateLimit-Reset";
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RxClient<RxObservableInvoker> client;
    @Autowired
    private ExecutorService executorService;

    @Value("${github.access.token:}")
    private String githubAccessToken;
    @Value("${spring.application.name:easemob}")
    private String applicationName;

    protected RxObservableInvoker configGithubHeaders(RxWebTarget<RxObservableInvoker> target) {
        RxInvocationBuilder<RxObservableInvoker> builder = target.request()
                .accept(GITHUB_V3_MEDIA_TYPE)
                .header("User-Agent", applicationName);
        if (!StringUtils.isEmpty(githubAccessToken)) {
            builder.header("Authorization", "token " + githubAccessToken);
        }
        return builder.rx(executorService);
    }

    /**
     * 对给定的参数uri发出GET请求, 并且把返回的值转换成指定的类型
     * <p>
     * 注意, 这里还处理了header中的link, 默认是处理rel='next'的link
     *
     * @param uri   需要请求的URI.
     * @param clazz 期望的返回值类型.
     * @return 返回一个Observable, 包含了该URI所对应的资源资源
     */
    public <T> Observable<T> fetch(final URI uri, final Class<T> clazz) {

        Observable<Response> responseObservable = getResponseObservable(uri);

        return responseObservable
                .filter(ASSERT_SUCCESS)
                .flatMap(r -> {
                    URI next = getNextUri(r, "next");
                    Observable<T> result = convert(clazz, r);
                    return result.mergeWith(fetch(next, clazz));
                });
    }

    /**
     * 从response的header中获取还有多少个request可以发, 参见 {@link https://developer.github.com/v3/#rate-limiting}
     */
    private static int getRemainingRequest(Response response){
        if(response == null){
            return 0;
        }
        String value =  response.getHeaderString(RATE_LIMIT_REMAINING_HEADER);
        if(StringUtils.isEmpty(value)){
            return 0;
        }
        return Integer.valueOf(value);
    }

    public static void main(String[] args) {
        Instant instant = Instant.ofEpochSecond(1448989180).plus(8, ChronoUnit.HOURS);
        System.out.println(instant);
    }

    /**
     * 把response中的数据转换成指定类型
     */
    private <T> Observable<T> convert(Class<T> clazz, Response response) {
        return Observable.just(response)
                .map(r -> r.readEntity(ContainerNode.class))
                .flatMap(r -> {
                    if (r.isArray()) {
                        return getJsonChildren(r);
                    } else {
                        return Observable.just(r);
                    }
                })
                .flatMap(pojoNode -> Observable.just(objectMapper.convertValue(pojoNode, clazz)));


    }

    private Observable<JsonNode> getJsonChildren(ContainerNode rootNode) {
//        final ArrayNode arrayNode = rootNode.arrayNode();
        List<JsonNode> children = new ArrayList<>(rootNode.size());
        Iterator<JsonNode> elements = rootNode.elements();

        while(elements.hasNext()){
            children.add(elements.next());
        }
        return Observable.from(children);
//        return Observable.create(new Observable.OnSubscribe<JsonNode>() {
//            @Override
//            public void call(Subscriber<? super JsonNode> subscriber) {
//                try {
//                    subscriber.onStart();
//
//                    if (arrayNode == null) {
//                        subscriber.onCompleted();
//                        return;
//                    }
//                    for (int i = 0; i < arrayNode.size(); i++) {
//                        subscriber.onNext(arrayNode.get(i));
//                    }
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        });
    }

    /**
     * 从输入的URI到发出一个GET请求, 并得到Response.
     */
    private Observable<Response> getResponseObservable(URI uri) {
        return Observable
                .just(uri)
                .filter(uri1 -> uri1 != null)
                .map(client::target)
                .map(this::configGithubHeaders)
                .flatMap(RxObservableInvoker::get);
    }


    /**
     * 从response的Link header中找到rel为next 的link, github使用这种方式来翻页
     */
    private static URI getNextUri(Response r, String rel) {
        String linkStr = r.getHeaderString("Link");
        Map<String, URI> map = parseGithubLinkHeader(linkStr);
        return map.get(rel);
    }

    //Link: <https://api.github.com/organizations/2704962/repos?page=1>; rel="first", <https://api.github.com/organizations/2704962/repos?page=1>; rel="prev"
    //jersey的org.glassfish.jersey.message.internal.LinkProvider.initBuilder方法只处理了有一个link的情况
    private static Map<String, URI> parseGithubLinkHeader(String link) {
        if (link == null || link.trim().length() == 0) {
            return Collections.emptyMap();
        }
        Map<String, URI> result = new HashMap<>();
        link = link.trim();
        int i = link.indexOf(',');
        if (i == -1) {
            parseOneLink(link, result);

        } else {
            String first = link.substring(0, i);
            parseOneLink(first, result);
            String rest = link.substring(i + 1, link.length());
            result.putAll(parseGithubLinkHeader(rest));
        }
        return result;
    }

    private static void parseOneLink(String link, Map<String, URI> result) {
        //only one link
        Link l = Link.valueOf(link);
        if (l != null) {
            result.put(l.getRel(), l.getUri());
        }
    }

    //过滤出成功的response
    private static Func1<Response, Boolean> ASSERT_SUCCESS = response -> response !=null && response.getStatusInfo()!=null && response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL;

}
