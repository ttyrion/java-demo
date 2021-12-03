package com.example.javademoapi;

import com.example.javademoutility.log.LoggerProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class JavaDemoApiApplication {

    public static void main(String[] args) {
        LoggerProxy.console("JavaDemoApiApplication", "main", args.toString(), null);
        SpringApplication.run(JavaDemoApiApplication.class, args);
    }

}
