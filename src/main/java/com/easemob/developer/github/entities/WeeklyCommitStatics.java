package com.easemob.developer.github.entities;

import lombok.Data;

/**
 * {
 "w": 1432425600,
 "a": 0,
 "d": 0,
 "c": 0
 }
 * @author stliu at apache.org
 * @since 6/4/15
 */
@Data
public class WeeklyCommitStatics {
    //timestamp, the week (end? start?)
    private long w;
    //addition
    private long a;
    //delete
    private long d;
    //change
    private long c;
}
