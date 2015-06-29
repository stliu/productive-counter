package com.easemob.developer.github.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class CommitUser implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -180887492938484405L;


    private String email;

    private String name;
    private String username;
    private String date;


}