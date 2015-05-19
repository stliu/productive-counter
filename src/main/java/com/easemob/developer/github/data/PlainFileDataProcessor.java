package com.easemob.developer.github.data;

import com.easemob.developer.github.Configuration;
import com.easemob.developer.github.event.Commit;
import com.easemob.developer.github.event.CommitInfo;
import com.easemob.developer.github.event.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.google.common.base.Strings;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * @author stliu @ apache.org
 */
@Component
@Slf4j
public class PlainFileDataProcessor implements EventDataProcessor {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RxClient<RxObservableInvoker> client;
    @Autowired
    private ExecutorService executorService;

    @Value("${github.access.token:}")
    private String githubAccessToken;
    @Value("${spring.application.name:easemob}")
    private String applicationName;

    @Override
    public void process(MultivaluedMap<String, String> headers, PushPayload body) throws Exception {
        if (headers == null || body == null) {
            log.warn("web hook is invalid, headers is  {} body is {}", headers, body);
            return;
        }

        final String commitUrl = body.getRepository().getCommitsUrl();

        final String repositoryName = body.getRepository().getFullName();
        final String baseUrl = commitUrl.substring(0, commitUrl.length() - 6);


        final RxWebTarget<RxObservableInvoker> target = client.target(baseUrl);

        rx.Observable<CommitInfo> observable = rx.Observable
                .from(body.getCommits())
                .flatMap(c -> {
                    String sha = c.getId();
                    return config(target.path(sha)).get();

                })
                .filter(ASSERT_SUCCESS)
                .map(r -> r.readEntity(CommitInfo.class));


        observable.subscribe(commitInfo -> {
            try {
                commitInfo.setRepositoryName(repositoryName);
                log.info(objectMapper.writeValueAsString(commitInfo));
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse commit info {}", commitInfo);
            }
        });

    }


    public static Func1<Response, Boolean> ASSERT_SUCCESS = response -> response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL;

    public RxObservableInvoker config(RxWebTarget<RxObservableInvoker> target) {
        RxInvocationBuilder<RxObservableInvoker> builder = target.request()
                .accept(Configuration.GITHUB_V3_MEDIA_TYPE)
                .header("User-Agent", applicationName);
        if (!Strings.isNullOrEmpty(githubAccessToken)) {
            builder.header("Authorization", "token " + githubAccessToken);
        }
        return builder.rx(executorService);
    }

}
