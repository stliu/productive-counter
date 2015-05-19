package com.easemob.developer.github.event;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.eclipse.egit.github.core.UserPlan;
import org.eclipse.egit.github.core.util.DateUtils;

/**
 * GitHub user model class.
 */
@Data
public class User implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -1211802439119529774L;

    /**
     * TYPE_USER
     */
    public static final String TYPE_USER = "User"; //$NON-NLS-1$

    /**
     * TYPE_ORG
     */
    public static final String TYPE_ORG = "Organization"; //$NON-NLS-1$

    private String avatarUrl;
    private String eventUrl;

    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String gravatarId;
    private String htmlUrl;
    private int id;
    private String login;
    private String origanizationsUrl;
    private String receivedEventsUrl;
    private String reposUrl;
    private boolean siteAdmin;
    private String starredUrl;
    private String subscriptionsUrl;
    private String type = TYPE_USER;
    private String url;


}
