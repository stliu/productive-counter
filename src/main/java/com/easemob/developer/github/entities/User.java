package com.easemob.developer.github.entities;

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
public class User implements Serializable {

    private static final long serialVersionUID = -1211802439119529774L;
    @Id
    private Integer id;
    @Column(unique = true)
    private String login;
    private String name;
    private String email;
    private String blog;
    @Transient
    private String avatarUrl;
    @Transient
    private String htmlUrl;
    @Transient
    private String url;

    public String getAvatarUrl() {
        if (avatarUrl == null) {
            avatarUrl = String.format("https://avatars.githubusercontent.com/u/%s?v=3", id);
        }
        return avatarUrl;
    }

    public String getHtmlUrl() {
        if (htmlUrl == null) {
            htmlUrl = "https://github.com/" + login;
        }
        return htmlUrl;
    }

    public String getUrl() {
        if (url == null) {
            url = "https://api.github.com/users/" + login;
        }
        return url;
    }
}
