package com.easemob.developer.github.data;

import com.easemob.developer.github.event.GollumPayload;
import com.easemob.developer.github.event.PushPayload;
import com.fasterxml.jackson.databind.node.ContainerNode;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author stliu @ apache.org
 */
public interface EventDataProcessor {
    void process(MultivaluedMap<String, String> headers, PushPayload body) throws Exception;
}
