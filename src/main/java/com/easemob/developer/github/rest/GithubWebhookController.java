package com.easemob.developer.github.rest;

import com.easemob.developer.github.data.EventDataProcessor;
import com.easemob.developer.github.entities.GithubRepository;
import com.easemob.developer.github.entities.PushPayload;
import com.easemob.developer.github.entities.RepositoryRepository;
import com.easemob.developer.github.request.FetchCommitInfoCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @Autowired
    private FetchCommitInfoCommand commitInfoCommand;
    @Autowired
    private RepositoryRepository githubRepository;

    /**
     * Github Push Event {@link https://developer.github.com/v3/activity/events/types/#pushevent} 的回调接口
     * <p>
     * 会根据回调中的commit id和url去获取该commit的信息并保存
     */
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

    /**
     * Github API 来获取一个commit的信息 {@link https://developer.github.com/v3/repos/commits/#get-a-single-commit}
     * GET /repos/:owner/:repo/commits/:sha
     */
    @RequestMapping(value = "/repos_data",
            produces = MediaType.APPLICATION_JSON_VALUE,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    @ResponseBody
    public List<GithubRepository> getCommitInfo() throws JsonProcessingException {
        log.debug("processing github webhook");
        return githubRepository.findAll();
    }


}
