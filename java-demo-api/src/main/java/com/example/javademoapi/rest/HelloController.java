package com.example.javademoapi.rest;

import com.example.javademodomain.hello.UserInfo;
import com.example.javademoservice.HelloService;
import com.example.javademoutility.log.LoggerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @Description:
 * @Date: Created on 15:51 2021/12/3
 */

@RestController
@RequestMapping(value = "hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping(value = "{user}",method = RequestMethod.GET)
    public Mono<UserInfo> user(@PathVariable String user,
                               ServerWebExchange serverWebExchange) throws Exception {

        String ip = serverWebExchange.getRequest().getRemoteAddress().getAddress().toString();
        LoggerProxy.console("HelloController", "user", user, ip);

        return Mono.just(helloService.user(user));
    }
}
