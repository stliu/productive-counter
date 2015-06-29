package com.easemob.developer.github.entities;


import lombok.Data;

import java.io.Serializable;

/**
 * Gollum Page model class.
 */
@Data
public class GollumPage implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5841603600916978606L;

    private String pageName;

    private String title;

    private String action;

    private String sha;

    private String htmlUrl;
    private String summary;

}
