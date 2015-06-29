package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rx.Observable;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Component
@Slf4j
public class FetchAllMembersCommand {
    private static final String FETCH_COMMIT_TEMPLATE = Github.GITHUB_ROOT + "/orgs/{org}/members";
    private final Github github;

    @Autowired
    public FetchAllMembersCommand(Github github) {
        this.github = github;
    }

    public Observable<User> fetch(String org) {

        if (StringUtils.isEmpty(org)) {
            throw new IllegalArgumentException("owner or repo must not empty");
        }
        URI uri = UriBuilder.fromUri(FETCH_COMMIT_TEMPLATE).resolveTemplate("org", org).build();
        return github
                .fetch(uri, User.class)
                .map(User::getUrl)
                .map(URI::create)
                .flatMap(userProfileUri -> github.fetch(userProfileUri, User.class));
    }


}
