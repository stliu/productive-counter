package com.easemob.developer.github.entities;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author stliu at apache.org
 * @since 12/22/15
 */
@Embeddable
@Data
public class CommitFile {
    private long id;
    private int additions;
    private int deletions;
    private int changes;
    private String status;
    private String rawUrl;
    private String blobUrl;
//    {
//        "filename": "file1.txt",
//            "additions": 10,
//            "deletions": 2,
//            "changes": 12,
//            "status": "modified",
//            "raw_url": "https://github.com/octocat/Hello-World/raw/7ca483543807a51b6079e54ac4cc392bc29ae284/file1.txt",
//            "blob_url": "https://github.com/octocat/Hello-World/blob/7ca483543807a51b6079e54ac4cc392bc29ae284/file1.txt",
//            "patch": "@@ -29,7 +29,7 @@\n....."
//    }



}
