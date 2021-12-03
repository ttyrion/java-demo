package com.example.javademodomain.hello;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Date: Created on 15:54 2021/12/3
 */

@Data
@AllArgsConstructor
public class UserInfo {
    private long id;
    private String name;
}
