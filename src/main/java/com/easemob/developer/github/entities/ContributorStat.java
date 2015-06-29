package com.easemob.developer.github.entities;

import lombok.Data;

import java.util.List;

/**
 * @author stliu at apache.org
 * @since 6/4/15
 */
@Data
public class ContributorStat {
    private long total;
    private List<WeeklyCommitStatics> weeks;
    private User author;
}
