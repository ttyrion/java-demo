package com.example.javademodomain.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Date: Created on 16:26 2021/12/8
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubUser {
    @Builder.Default
    private int id = 0;
    private String login;
    private String avatar_url;
}
