package com.easemob.developer.github.rest;

import com.easemob.developer.github.data.EventDataProcessor;
import com.easemob.developer.github.event.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author stliu <stliu@apache.org>
 * @date 5/20/15
 */
@RestController
@Slf4j
public class GithubWebhookController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventDataProcessor processor;

    @RequestMapping(value = "/github",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity webhook(@RequestBody PushPayload json, @RequestHeader HttpHeaders headers) throws JsonProcessingException {
        log.debug("processing github webhook");
        try {
            processor.process(headers, json);
        } catch (Exception e) {
            log.error("Failed to process web hook request, headers are {}, and body is {}", headers.toString(), objectMapper.writeValueAsString(json));
        }
        return ResponseEntity.accepted().build();
    }
}
