package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.GithubRepository;
import com.easemob.developer.github.entities.RepositoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * GET /orgs/:org/repos
 *
 * @author stliu at apache.org
 * @since 6/4/15
 */
@Component
@Slf4j
public class GetAllReposCommand {
    private static final UriBuilder API_BUILDER = UriBuilder.fromUri(Github.GITHUB_ROOT).path("/orgs/{owner}/repos");
    private final StringRedisTemplate template;
    private final Github github;

    @Autowired
    RepositoryRepository repositoryRepository;

    @Autowired
    public GetAllReposCommand(Github github, StringRedisTemplate template) {
        this.template = template;
        this.github = github;
    }


    @Autowired
    private GetStatForRepoCommand command;


    public void aaa() {
        URI uri = API_BUILDER.resolveTemplate("owner", "easemob").build();
        Observable<GithubRepository> observable = github.fetch(uri, GithubRepository.class);
        observable.subscribe(new Subscriber<GithubRepository>() {
            @Override
            public void onCompleted() {
                System.out.println("======= complete");
                repositoryRepository.flush();
                System.out.println("======= count ========");

                long count = repositoryRepository.count();
                System.out.println("======= count ========" + count);

                repositoryRepository.findAll().stream().forEach(c -> {
                    System.out.println(c);
                });
            }

            @Override
            public void onError(Throwable throwable) {

                throwable.printStackTrace();
            }

            @Override
            public void onNext(GithubRepository repository) {
                log.info("persisting " + repository);
                repositoryRepository.save(repository);
                log.info("persisting " + repository + "done");
            }
        });

    }

    public Observable<GithubRepository> allRepos(String owner) {
        URI uri = API_BUILDER.resolveTemplate("owner", owner).build();
        return github.fetch(uri, GithubRepository.class);
    }

    public void fetch(String owner) {
        final String repos_key = owner + ":repos";

        allRepos(owner).subscribe(new Subscriber<GithubRepository>() {
            @Override
            public void onCompleted() {
                System.out.println("========  completed =====");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(GithubRepository repository) {
                final String repoName = repository.getName();
                final String repo_key = repos_key + ":" + repoName;
                Map<String, String> map = new HashMap<>();
                map.put("description", repository.getDescription());
                map.put("html_url", repository.getHtmlUrl());
                template.opsForHash().putAll(repo_key, map);
                template.opsForSet().add(repos_key, repoName);

                command.fetch(owner, repoName);
            }
        });
    }


}
