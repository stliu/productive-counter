package com.easemob.developer.github.entities;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author stliu at apache.org
 * @since 6/5/15
 */
@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<GithubRepository, String>{
}
