package com.example.javademodomain.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Date: Created on 16:26 2021/12/8
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUser {
    private int id;
    private String login;
    private String avatar_url;
}
