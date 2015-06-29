package com.easemob.developer.github.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author stliu at apache.org
 * @since 6/10/15
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class CommitInfo {
    private CommitUser author;
    private CommitUser committer;
}
