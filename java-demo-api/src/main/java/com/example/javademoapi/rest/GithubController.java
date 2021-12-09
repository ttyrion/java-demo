package com.example.javademoapi.rest;

import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.hello.UserInfo;
import com.example.javademoservice.UserInfoService;
import com.example.javademoutility.log.LoggerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Date: Created on 16:50 2021/12/8
 */


@RestController
@RequestMapping(value = "/github")
public class GithubController {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
    public Mono<GithubUser> user(@PathVariable int userId,
                                 ServerWebExchange serverWebExchange) throws Exception {

        String ip = serverWebExchange.getRequest().getRemoteAddress().getAddress().toString();
        LoggerProxy.console("GithubController", "user", "" + userId, ip);

        return Mono.just(userInfoService.user(userId));
    }
}
