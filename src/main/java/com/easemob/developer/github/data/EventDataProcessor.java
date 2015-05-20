package com.easemob.developer.github.data;

import com.easemob.developer.github.event.PushPayload;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author stliu @ apache.org
 */
public interface EventDataProcessor {
    void process(MultivaluedMap<String, String> headers, PushPayload body) throws Exception;
}
