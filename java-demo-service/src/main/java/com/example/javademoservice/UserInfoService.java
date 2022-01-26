package com.example.javademoservice;

import com.example.javademocommand.github.GithubCommand;
import com.example.javademocommand.github.GithubObservableCommand;
import com.example.javademodomain.github.GithubUser;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:
 * @Date: Created on 16:48 2021/12/8
 */


@Component
public class UserInfoService {
    public GithubUser user(int userId) {
        GithubCommand githubCommand = new GithubCommand(userId);
        return githubCommand.execute();
    }

    public List<GithubUser> userList(int userId) {
        GithubObservableCommand githubObservableCommand = new GithubObservableCommand(userId);

        List<GithubUser> userList = new ArrayList<>();

        Observable<GithubUser> o1 = githubObservableCommand.observe();
        if (o1 == null) {
            System.out.println("UserInfoService: command observe error.");
            return new ArrayList<>();
        }

        // AtomicReference<Boolean> refCompleted = new AtomicReference<>();
        /**
        * 在onError或者onCompleted被调用之后，subscribe方法才会返回
         * 因此才能在onNext方法中接收多个结果
        */
        o1.subscribe(new Observer(){

            @Override
            public void onCompleted() {
                System.out.println("UserInfoService: onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("UserInfoService: onError" + e);
            }

            @Override
            public void onNext(Object o) {
                System.out.println("UserInfoService: onNext_" + o);
                if (o instanceof GithubUser) {
                    userList.add((GithubUser)o);
                }
            }
        });

        System.out.println("UserInfoService:" + userId);

        return userList;
    }
}
