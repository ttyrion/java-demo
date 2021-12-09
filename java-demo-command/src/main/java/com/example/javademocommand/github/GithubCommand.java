package com.example.javademocommand.github;

import com.example.javademoclient.github.GithubClient;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.bean.DemoApplicationContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @Description: Hystrix: https://github.com/Netflix/Hystrix/wiki/
 * @Date: Created on 10:56 2021/12/8
 */


public class GithubCommand extends HystrixCommand<GithubUser> {
    private final int userId;

    public GithubCommand(int userId) {
        super(HystrixCommandGroupKey.Factory.asKey("GithubGroup"));
        this.userId = userId;
    }

    @Override
    protected GithubUser run() throws Exception {
        GithubClient githubClient = DemoApplicationContext.getBean(GithubClient.class);
        GithubUserResponse response = githubClient.user(userId, null);
        if (response != null) {
            return new GithubUser(response.getId(), response.getLogin(), response.getAvatar_url());
        }

        return null;
    }
}
