package com.easemob.developer.github.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Commit implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -1893280210470143372L;

    private CommitUser author;

    private CommitUser committer;

    private String message;

    private String id;

    private String url;

    private Date timestamp;
    private boolean distinct;
    private List<String> added;
    private List<String> modified;
    private List<String> removed;




}