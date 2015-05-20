package com.easemob.developer.github.event;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/18/15
 */
@Data
public class Repository implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 406671816413754925L;

    private boolean fork;
    private int forks;


    private boolean isPrivate;

    private Date createdAt;

    private Date pushedAt;

    private Date updatedAt;


    private int openIssues;
    private int openIssuesCount;

    private int size;

    private int watchers;
    private int watchersCount;


    private String htmlUrl;


    private String masterBranch;


    private String name;

    private String sshUrl;

    private String svnUrl;

    private String url;

    private User owner;
    private String archiveUrl;

    private String assigneesUrl;
    private String blobsUrl;
    private String branchesUrl;
    private String cloneUrl;
    private String collaboratorsUrl;
    private String commentsUrl;
    private String commitsUrl;
    private String compareUrl;
    private String contentsUrl;
    private String contributorsUrl;
    private String defaultBranch;
    private String description;
    private String downloadsUrl;
    private String eventsUrl;
    private int forksCount;
    private String forksUrl;
    private String fullName;
    private String gitCommitsUrl;
    private String gitRefsUrl;
    private String gitTagsUrl;
    private String gitUrl;
    private boolean hasDownloads;
    private boolean hasIssues;
    private boolean hasPages;
    private boolean hasWiki;
    private String homepage;
    private String hookUrl;

    private String issueCommentUrl;
    private String issueEventsUrl;
    private String issuesUrl;
    private String keysUrl;
    private String labelsUrl;
    private String language;
    private String languageUrl;
    private String mergesUrl;
    private String milestonesUrl;
    private String mirrorUrl;
    private String notificationsUrl;
    private String pullsUrl;
    private String releasesUrl;
    private int stargazersCount;
    private String stargazers_url;
    private String statuses_url;
    private String subscribers_url;

    private String subscription_url;

    private String tags_url;
    private String teams_url;
    private String trees_url;

}
