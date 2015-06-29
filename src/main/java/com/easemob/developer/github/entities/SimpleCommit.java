package com.easemob.developer.github.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * 一个简化的commit类, 只包含了sha和url信息
 * </p>
 * 因为通过github的/commits api来获取的commit信息中不包括stat信息,
 * 必须得通过 /commits/{sha} 才能获取到stat信息
 *
 * 所以, 这里先通过 /commits获取到所有的sha, 再调用 /commits/sha来获取更详细的commit信息
 * @author stliu at apache.org
 * @since 6/9/15
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public  class SimpleCommit{
    private String sha;
    private String url;
}