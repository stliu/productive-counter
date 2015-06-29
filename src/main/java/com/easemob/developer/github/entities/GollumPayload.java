package com.easemob.developer.github.entities;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * GollumEvent payload model class.
 */
@Data
public class GollumPayload extends EventPayload implements Serializable {
    private static final long serialVersionUID = 7111499446827257290L;
    private List<GollumPage> pages;
    private User sender;
    private GithubRepository repository;
}
