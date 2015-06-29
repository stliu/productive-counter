package com.easemob.developer.github.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/18/15
 */
@Data
@Entity
@Table(name = "repository")
public class GithubRepository implements Serializable {
    private static final long serialVersionUID = 406671816413754925L;
    @Id
    private String fullName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pushedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private String htmlUrl;
    private String name;
    private String url;
    private String commitsUrl;
    private String contributorsUrl;
    private String description;
    private String eventsUrl;
    private String gitCommitsUrl;
}
