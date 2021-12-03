package com.example.javademoservice;

import com.example.javademodomain.hello.UserInfo;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Date: Created on 16:06 2021/12/3
 */

@Component
public class HelloService {
    public UserInfo user(String name) {
        return new UserInfo(System.currentTimeMillis(), name);
    }
}
