package com.easemob.developer.github.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;

/**
 * GitHub user model class.
 */
@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class User implements Serializable {

    private static final long serialVersionUID = -1211802439119529774L;
    @Id
    private Integer id;
    @Column(unique = true)
    private String login;
    private String name;
    private String email;
    private String blog;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("gravatar_id")
    private String gravatorId;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String url;
    @JsonProperty("followers_url")
    private String followersUrl;
    @JsonProperty("following_url")
    private String followingUrl;
    @JsonProperty("gists_url")
    private String gistsUrl;
    @JsonProperty("starred_url")
    private String starredUrl;
    @JsonProperty("subscriptions_url")
    private String subscriptionsUrl;
    @JsonProperty("organizations_url")
    private String organizationsUrl;
    @JsonProperty("repos_url")
    private String reposUrl;
    @JsonProperty("events_url")
    private String eventsUrl;
    @JsonProperty("received_events_url")
    private String receivedEventsUrl;
    private String type;
    @JsonProperty("site_admin")
    private boolean siteAdmin;
}
