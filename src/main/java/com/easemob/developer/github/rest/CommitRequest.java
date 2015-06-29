package com.easemob.developer.github.rest;

import lombok.Data;

/**
 * @author stliu at apache.org
 * @since 6/1/15
 */
@Data
public class CommitRequest {
    private String owner;
    private String repo;
    private String sha;
}
