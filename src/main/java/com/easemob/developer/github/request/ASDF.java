package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.*;
import com.easemob.developer.github.repositories.CommitRepository;
import com.easemob.developer.github.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Instant;

/**
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Component
public class ASDF {
    @Autowired
    FetchAllCommitsCommand allCommitsCommand;
    @Autowired
    CommitRepository commitRepository;
    @Autowired
    Github github;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FetchAllMembersCommand allMembersCommand;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GetAllReposCommand allReposCommand;
    @Autowired
    RepositoryRepository repositoryRepository;

    private static final String OWNER = "easemob";

    @PostConstruct
    public void findAllMembers() {
        allMembersCommand.fetch(OWNER)
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("======== all members complete =====");
                        allReposCommand.allRepos(OWNER)
                                .flatMap(FETCH_ALL_COMMITS)
                                .subscribe(COMMIT_SUBSCRIBER);


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        System.out.println("========= user ======");
                        userRepository.saveAndFlush(user);
                        try {
                            System.out.println(objectMapper.writeValueAsString(user));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public final Subscriber<Commit> COMMIT_SUBSCRIBER = new Subscriber<Commit>() {
        @Override
        public void onCompleted() {
            System.out.println("======== complete =====");
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onNext(Commit commit) {
            System.out.println("========= commit ======");
            try {
                commitRepository.saveAndFlush(commit);
                System.out.println(objectMapper.writeValueAsString(commit));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    static Instant  since = Instant.parse("2015-06-01T00:00:00.00Z");

    /**
     * 把一个github repo的所有的commits取出来
     */
    public final Func1<GithubRepository, Observable<Commit>> FETCH_ALL_COMMITS = new Func1<GithubRepository, Observable<Commit>>() {
        @Override
        public Observable<Commit> call(GithubRepository repository) {
            System.out.println("========= repo ====== " +repository.getName());
            try {
                repositoryRepository.saveAndFlush(repository);
                System.out.println(objectMapper.writeValueAsString(repository));
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            Instant since = repository.getCreatedAt().toInstant();
            Observable<SimpleCommit> observable = allCommitsCommand.fetch(OWNER, repository.getName(), since, null, null);
            return observable
                    .map(SimpleCommit::getUrl)
                    .map(URI::create).flatMap(u -> github.fetch(u, Commit.class));
        }
    };
}
