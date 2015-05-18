package com.easemob.developer.github.data;

import com.fasterxml.jackson.databind.node.ContainerNode;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author stliu @ apache.org
 */
public interface EventDataProcessor {
    void process(MultivaluedMap<String, String> headers, ContainerNode body) throws Exception;
}
