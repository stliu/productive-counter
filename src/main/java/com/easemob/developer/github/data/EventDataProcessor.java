package com.easemob.developer.github.data;

import com.easemob.developer.github.entities.PushPayload;
import org.springframework.http.HttpHeaders;


/**
 * @author stliu @ apache.org
 */
public interface EventDataProcessor {
    void process(HttpHeaders headers, PushPayload body) throws Exception;
}
