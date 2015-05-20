package com.easemob.developer.github.rest;

import com.easemob.developer.github.data.EventDataProcessor;
import com.easemob.developer.github.event.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public GithubWebhookResource() {
        logger.info("GithubWebhookResource initialized");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventDataProcessor processor;

    @POST
    public Response webhook(PushPayload json, @Context HttpHeaders headers) throws JsonProcessingException {
        logger.debug("processing github webhook");
        try {
            processor.process(headers.getRequestHeaders(), json);
        } catch (Exception e) {
            logger.error("Failed to process web hook request, headers are {}, and body is {}", headers.getRequestHeaders().toString(), objectMapper.writeValueAsString(json));
        }
        return Response.ok().build();
    }
}
