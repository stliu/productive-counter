package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.ContributorStat;
import com.easemob.developer.github.entities.GithubRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * /repos/easemob/sdkexamples-ios/stats/contributors
 * <p>
 * {@see https://developer.github.com/v3/repos/statistics/#contributors}
 *
 * @author stliu at apache.org
 * @since 6/4/15
 */
@Slf4j
@Component

public class GetStatForRepoCommand {
    private static final String REPO_API = "/stats/contributors";
    private static final String FULL_API = Github.GITHUB_ROOT + "/repos/{owner}/{repo}" + REPO_API;


    private final Github github;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public GetStatForRepoCommand(Github github) {
        this.github = github;
    }

    public void fetch(String owner, String repo) {
        final URI uri = UriBuilder.fromUri(FULL_API)
                .resolveTemplate("owner", owner)
                .resolveTemplate("repo", repo).build();

        fetchByURI(uri);
    }

    private void fetchByURI(URI uri) {
        github.fetch(uri, ContributorStat.class)
                .subscribe(contributorStat -> {
                    try {
                        log.info(objectMapper.writeValueAsString(contributorStat));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                });
    }

    public void fetchByRepository(GithubRepository repository) {
        final URI uri = UriBuilder.fromUri(repository.getUrl() + REPO_API).build();
        fetchByURI(uri);
    }

}
