package com.easemob.developer.github.data;

import com.easemob.developer.github.event.Commit;
import com.easemob.developer.github.event.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Subscriber;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * @author stliu @ apache.org
 */
@Component
public class PlainFileDataProcessor implements EventDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PlainFileDataProcessor.class);

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RxClient<RxObservableInvoker> client;

    @Override
    public void process(MultivaluedMap<String, String> headers, PushPayload body) throws Exception {
        if (headers == null || body == null) {
            logger.warn("web hook is invalid, headers is  {} body is {}", headers, body);
            return;
        }

        final String commitUrl = body.getRepository().getGitCommitsUrl();

        final String baseUrl = commitUrl.substring(0, commitUrl.length() - 6);
        final RxWebTarget<RxObservableInvoker> target = client.target(baseUrl);
        rx.Observable.from(body.getCommits()).flatMap(c -> {
            String sha = c.getId();
            return target.path(sha).request().accept("*/*").header("User-Agent", "curl/7.37.1").rx().get();

        }).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                System.out.println("===========");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(Response response) {
                System.out.println(response.getStatus());
                ContainerNode node = response.readEntity(ContainerNode.class);
                try {
                    System.out.println(objectMapper.writeValueAsString(node));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
        body.getCommits().forEach(new Consumer<Commit>() {
            @Override
            public void accept(Commit commit) {
                commit.getUrl();
            }
        });
        String line = "----------------------------------\r\n" + headers.toString() + "\r\n" + objectMapper.writeValueAsString(body);
        logger.info(line);
//        Files.append(, file, UTF_8);;
    }
}
