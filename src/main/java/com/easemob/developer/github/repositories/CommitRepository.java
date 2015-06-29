package com.easemob.developer.github.repositories;

import com.easemob.developer.github.entities.Commit;
import com.easemob.developer.github.entities.GithubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Repository
public interface CommitRepository  extends JpaRepository<Commit, String> {
}
