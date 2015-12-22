package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.*;
import com.easemob.developer.github.repositories.CommitRepository;
import com.easemob.developer.github.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                                .subscribeOn(Schedulers.io())
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

                if (commit.getAuthor() != null && userRepository.exists(commit.getAuthor().getId())) {
                    commitRepository.saveAndFlush(commit);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
    static Instant since = Instant.parse("2015-12-01T00:00:00.00Z");

//    static Instant since = Instant.parse("2015-09-01T00:00:00.00Z");
//    static Instant  until = Instant.parse("2015-06-01T00:00:00.00Z");

    /**
     * 把一个github repo的所有的commits取出来
     */
    public final Func1<GithubRepository, Observable<Commit>> FETCH_ALL_COMMITS = new Func1<GithubRepository, Observable<Commit>>() {
        @Override
        public Observable<Commit> call(GithubRepository repository) {
            System.out.println("========= repo ====== " + repository.getName());
            try {
                repositoryRepository.saveAndFlush(repository);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            Observable<SimpleCommit> observable = github.fetch(repoWithBranchUri(repository), BranchInfo.class)
                    .map(b -> b.getCommit().getUrl())
                    .flatMap(u -> allCommitsCommand.fetchWithBranch(u, since, null, null));


            return observable
                    .map(SimpleCommit::getUrl)
                    .map(URI::create)
                    .flatMap(u -> github.fetch(u, Commit.class));
        }

        private URI repoWithBranchUri(GithubRepository repository) {
            return URI.create(repository.getUrl() + "/branches");
        }
    };

    public final static class BranchInfo{
        private String name;
        private Commit commit;

        public Commit getCommit() {
            return commit;
        }

        public void setCommit(Commit commit) {
            this.commit = commit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            System.out.println("======= branch "+name);
            this.name = name;
        }

        public final static class Commit{
            private String sha;
            private String url;

            public String getSha() {
                return sha;
            }

            public void setSha(String sha) {
                this.sha = sha;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
