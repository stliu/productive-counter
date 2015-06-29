package com.easemob.developer.github.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * @author stliu <stliu@apache.org>
 * @date 5/19/15
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Entity
@Immutable
public class Commit implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1893280210470143372L;
    @Id
    private String sha;
    @Embedded
    private CommitStat stats;
    @ManyToOne
    @JoinColumn(nullable = true)
    private User author;
    @ManyToOne()
    @JoinColumn(nullable = true)
    private User committer;
    private String url;

    @Transient
    private CommitInfo commit;
    @Access(AccessType.PROPERTY)
    private String authorName;
    @Access(AccessType.PROPERTY)
    private String committerName;
    @Access(AccessType.PROPERTY)
    private Date authorDate;
    @Access(AccessType.PROPERTY)
    private Date commitDate;

    public String getAuthorName() {
        if (authorName == null && commit != null && commit.getAuthor() != null) {
            authorName = commit.getAuthor().getName();
        }
        return authorName;
    }

    public String getCommitterName() {
        if (committerName == null && commit != null && commit.getCommitter() != null) {
            committerName = commit.getCommitter().getName();
        }
        return committerName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAuthorDate() {
        if (authorDate == null && commit != null && commit.getAuthor() != null) {
            authorDate = Date.from(Instant.parse(commit.getAuthor().getDate()));

        }
        return authorDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCommitDate() {
        if (commitDate == null && commit != null && commit.getCommitter() != null) {
            commitDate = Date.from(Instant.parse(commit.getCommitter().getDate()));
        }
        return commitDate;
    }

}