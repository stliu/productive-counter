package com.easemob.developer.github.request;

import com.easemob.developer.github.entities.User;
import com.easemob.developer.github.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.UriBuilder;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author stliu @ apache.org
 */
@Component
@Slf4j
public class FetchAllGithubUsersCommand {
    private static final String FETCH_ALL_USERS_TEMPLATE = Github.GITHUB_ROOT + "/users";
    private final Github github;
    private final UserRepository userRepository;
    private final int batchSize;
    @Autowired
    public FetchAllGithubUsersCommand(Github github, UserRepository userRepository, @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int batchSize) {
        this.github = github;
        this.userRepository = userRepository;
        this.batchSize = batchSize;
    }

    public Observable<User> fetch(long since) {

        URI uri = UriBuilder.fromUri(FETCH_ALL_USERS_TEMPLATE).queryParam("since", since).build();
        return github
                .fetch(uri, User.class);
    }
    @PostConstruct
    public void run(){
//
//        long count = userRepository.count();
//        long maxUserId = userRepository.getMaxUserIdFromDB();
//        BitSet bitSet = new BitSet();
//        long max = Math.max(count, maxUserId);
//        userRepository.listIds().forEach(bitSet::set);
//
//       int size = bitSet.size();
//        System.out.println("==============="+size);
//        if(size < max){
//            LongStream.rangeClosed(size, max).forEach(i->{
//                bitSet.set((int)i, false);
//            });
//        }
//
//        size = bitSet.size();
//        System.out.println("--------"+size);
//
        fetch(0).observeOn(Schedulers.io())
                .buffer(batchSize).subscribe(new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {
                System.out.println("=======================");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(List<User> users) {
                userRepository.save(users);
                userRepository.flush();
            }
        });
    }
}
