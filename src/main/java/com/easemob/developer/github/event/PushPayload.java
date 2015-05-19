package com.easemob.developer.github.event;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/18/15
 */
@Data
public class PushPayload extends EventPayload implements Serializable {

    private static final long serialVersionUID = -1542484898531583478L;

    private String after;
    private String before;
    private String baseRef;

    private String ref;


    private List<Commit> commits;
    private Commit headCommit;

    private CommitUser pusher;
    private User sender;
    private Repository repository;

    private String compare;
    private boolean created;
    private boolean deleted;
    private boolean forced;
}
