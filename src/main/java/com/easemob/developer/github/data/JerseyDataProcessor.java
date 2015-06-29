package com.easemob.developer.github.data;

import com.easemob.developer.github.entities.PushPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author stliu @ apache.org
 */
@Component
@Slf4j
public class JerseyDataProcessor implements EventDataProcessor {


    @Override
    public void process(HttpHeaders headers, PushPayload body) throws Exception {
        if (headers == null || body == null) {
            log.warn("web hook is invalid, headers is  {} body is {}", headers, body);
            return;
        }

    }


}
