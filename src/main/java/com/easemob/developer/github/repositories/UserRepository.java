package com.easemob.developer.github.repositories;

import com.easemob.developer.github.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    @Query("select u.id from User u")
    Stream<Integer> listIds();

    @Query("select max(u.id) from User u")
    Integer getMaxUserIdFromDB();
}
