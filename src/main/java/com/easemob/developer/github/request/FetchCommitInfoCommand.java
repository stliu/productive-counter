package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.Commit;
import com.easemob.developer.github.rest.CommitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;


/**
 * 调用 Github API 来获取一个commit的信息, 全部的commit信息, 包括修改的行数
 * <p>
 * 具体参见 https://developer.github.com/v3/repos/commits/#get-a-single-commit
 * <p>
 * 这个的response参见 https://gist.github.com/stliu/242ae4937ace9db6107e
 * <p>
 * GET /repos/:owner/:repo/commits/:sha
 *
 * @author stliu at apache.org
 * @since 6/1/15
 */
@Component
@Slf4j
public class FetchCommitInfoCommand {
    private static final UriBuilder FETCH_COMMIT_TEMPLATE = UriBuilder.fromUri(Github.GITHUB_ROOT + "/repos/{owner}/{repo}/commits/{sha}");
    private final Github github;

    @Autowired
    public FetchCommitInfoCommand(Github github) {
        this.github = github;
    }


    public Observable<Commit> fetch(final CommitRequest request) {
        final URI uri = FETCH_COMMIT_TEMPLATE
                .resolveTemplate("owner", request.getOwner())
                .resolveTemplate("repo", request.getRepo())
                .resolveTemplate("sha", request.getSha()).build();
        return github.fetch(uri, Commit.class);
    }
}
