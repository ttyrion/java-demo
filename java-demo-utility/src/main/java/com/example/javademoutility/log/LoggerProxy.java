package com.example.javademoutility.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: You can think of slf4j as an Java interface,
 * and then you would need an implementation (ONLY ONE) at runtime
 * to provide the actual logging details, such as writing to STDOUT or to a file etc.
 *
 * Each logging implementation (or called binding) would obviously
 * have their own way of configuring the log output, but your application
 * will remain agnostic and always use the same org.slf4j.Logger API.
 *
 * @Date: Created on 13:38 2021/12/3
 */

public class LoggerProxy {

    private static final Logger fileLogger
            = LoggerFactory.getLogger("INFO_FILE");

    private static final Logger stdLogger
            = LoggerFactory.getLogger("STDOUT");

    public static void info(String className, String methodName, String args, Object obj) {
        fileLogger.info("class: {}, method: {}, args: {}, info: {}.", className, methodName, args, obj);
    }

    public static void console(String className, String methodName, String args, Object obj) {
        stdLogger.info("class: {}, method: {}, args: {}, info: {}.", className, methodName, args, obj);
    }
}
