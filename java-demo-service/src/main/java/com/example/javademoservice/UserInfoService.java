package com.example.javademoservice;

import com.example.javademoclient.github.GithubClient;
import com.example.javademocommand.github.GithubCommand;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.bean.DemoApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Date: Created on 16:48 2021/12/8
 */


@Component
public class UserInfoService {
    public GithubUser user(int userId) {
        GithubClient githubClient = DemoApplicationContext.getBean(GithubClient.class);
        GithubUserResponse response = githubClient.user(userId, null);
        if (response != null) {
            return new GithubUser(response.getId(), response.getLogin(), response.getAvatar_url());
        }

        return null;
    }
}
