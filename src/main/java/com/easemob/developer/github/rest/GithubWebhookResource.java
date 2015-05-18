package com.easemob.developer.github.rest;

import com.easemob.developer.github.data.EventDataProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author stliu @ apache.org
 */
@Path("/github")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class GithubWebhookResource {

    private static final Logger logger = LoggerFactory.getLogger(GithubWebhookResource.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventDataProcessor processor;

    @POST
    public Response webhook(ContainerNode json, @Context HttpHeaders headers) throws JsonProcessingException {
        try {
            processor.process(headers.getRequestHeaders(), json);
        } catch (Exception e) {
            logger.error("Failed to process web hook request, headers are {}, and body is {}", headers.getRequestHeaders().toString(), objectMapper.writeValueAsString(json));
        }
        return Response.ok().build();
    }
}