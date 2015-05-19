package com.easemob.developer.github.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author stliu @ apache.org
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class CommitInfo {
    private String sha;
    private Commit commit;
    private CommitStat stats;
    private String repositoryName;
}
