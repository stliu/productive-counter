package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.Commit;
import com.easemob.developer.github.entities.SimpleCommit;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rx.Observable;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.Instant;

/**
 * https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
 *
 * 获取一个repo的commits, 支持分页
 *
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Component
@Slf4j
public class FetchAllCommitsCommand {


    private final Github github;

    @Autowired
    public FetchAllCommitsCommand(Github github) {
        this.github = github;
    }

    public Observable<SimpleCommit> fetch(String owner, String repo, String branch, Instant since, Instant until, Integer perPage) {
         UriBuilder FETCH_COMMIT_TEMPLATE = UriBuilder.fromUri(Github.GITHUB_ROOT + "/repos/{owner}/{repo}/commits/{branch}");

        if (StringUtils.isEmpty(owner) || StringUtils.isEmpty(repo)) {
            throw new IllegalArgumentException("owner or repo must not empty");
        }
        UriBuilder builder = FETCH_COMMIT_TEMPLATE.resolveTemplate("owner", owner).resolveTemplate("repo", repo);
        if (since != null) {
            builder.queryParam("since", since);
        }
        if (until != null) {
            builder.queryParam("until", until);
        }
        if (perPage != null && perPage > 0) {
            builder.queryParam("per_page", perPage);
        }
        URI uri = builder.build();
        System.out.println("fetch commits: "+uri);
        return  github.fetch(uri, SimpleCommit.class);
    }

    public Observable<SimpleCommit> fetchWithBranch(String url, Instant since, Instant until, Integer perPage) {
        UriBuilder FETCH_COMMIT_TEMPLATE = UriBuilder.fromUri(url);

        UriBuilder builder = FETCH_COMMIT_TEMPLATE;
        if (since != null) {
            builder.queryParam("since", since);
        }
        if (until != null) {
            builder.queryParam("until", until);
        }
        if (perPage != null && perPage > 0) {
            builder.queryParam("per_page", perPage);
        }
        URI uri = builder.build();
        System.out.println("fetch commits: "+uri);
        return  github.fetch(uri, SimpleCommit.class);
    }



}
