package com.easemob.developer.github.data;

import com.easemob.developer.github.event.PushPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import rx.Observable;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/20/15
 */
@Component
public class RestTemplateEventDataProcessor implements EventDataProcessor {
    @Autowired
    AsyncRestTemplate rest;

    @Override
    public void process(HttpHeaders headers, PushPayload body) throws Exception {
        ListenableFuture<ClientHttpResponse> future = rest.getAsyncRequestFactory().createAsyncRequest(null, HttpMethod.GET).executeAsync();
        Observable.from(future);
    }
}
