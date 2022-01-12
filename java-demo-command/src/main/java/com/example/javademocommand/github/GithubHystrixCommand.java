package com.example.javademocommand.github;

import com.example.javademoclient.github.GithubClient;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@DefaultProperties(
        groupKey = "githubUser", threadPoolKey = "github-user-pool",
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "50"),
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "20")
        },
        threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "20"),
                @HystrixProperty(name = "maxQueueSize", value = "500"),
                @HystrixProperty(name = "keepAliveTimeMinutes", value = "3"),
                // https://github.com/Netflix/Hystrix/wiki/Configuration#queueSizeRejectionThreshold
                @HystrixProperty(name = "queueSizeRejectionThreshold", value = "500"),
        }
)
public class GithubHystrixCommand{

    @Autowired
    private GithubClient githubClient;

    // @HystrixCommand(commandKey = "user", fallbackMethod = "userFallback", ignoreExceptions = {MyException.class})
    @HystrixCommand(commandKey = "user", fallbackMethod = "userFallback")
    public Future<GithubUser> user(int userId, int timeOutMs) throws Exception {

        return new AsyncResult<GithubUser>() {
            @Override
            public GithubUser get() {
                return invoke();
            }

            @Override
            public GithubUser invoke() {
                GithubUserResponse response = githubClient.user(userId, null);
                if (response != null) {
                    return new GithubUser(response.getId(), response.getLogin(), response.getAvatar_url());
                }

                return null;
            }
        };
    }

    protected GithubUser userFallback() {
        return new GithubUser();
    }
}
