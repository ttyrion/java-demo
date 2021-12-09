package com.example.javademodomain.github;

import lombok.Data;

/**
 * @Description: 只取部分字段:
 * Retrofit won't complain about missing properties – since it only maps what we need
 * @Date: Created on 17:08 2021/12/5
 */


@Data
public class GithubUserResponse {
    private int id;
    private String login;
    private String avatar_url;
}
