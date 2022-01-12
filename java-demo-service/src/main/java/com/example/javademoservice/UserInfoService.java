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

/**
 * @Description:
 * @Date: Created on 16:48 2021/12/8
 */


@Component
public class UserInfoService {
    public GithubUser user(int userId) {
        GithubObservableCommand githubObservableCommand = new GithubObservableCommand(userId);

        List<GithubUser> userList = new ArrayList<>();

        Observable<GithubUser> o1 = githubObservableCommand.observe();
        if (o1 == null) {
            System.out.println("UserInfoService: command observe error.");
            return GithubUser.builder().build();
        }

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
                if (o instanceof GithubUser) {
                    System.out.println("UserInfoService: onNext_" + o);
                    userList.add((GithubUser)o);
                }
            }
        });

        System.out.println("UserInfoService:" + userId);

        GithubCommand githubCommand = new GithubCommand(userId);
        return githubCommand.execute();
    }
}
