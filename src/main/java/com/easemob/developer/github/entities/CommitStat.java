package com.easemob.developer.github.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Embeddable;

/**
 * @author stliu @ apache.org
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Embeddable
public class CommitStat {
    private int total;
    private int additions;
    private int deletions;
}
