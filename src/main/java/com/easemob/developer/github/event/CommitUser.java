package com.easemob.developer.github.event;

import lombok.Data;

import java.io.Serializable;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@Data
public class CommitUser implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -180887492938484405L;


    private String email;

    private String name;
    private String username;

}