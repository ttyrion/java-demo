package com.example.javademocommand.github;

import com.example.javademoclient.github.GithubClient;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.bean.DemoApplicationContext;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Observer;
import rx.observables.SyncOnSubscribe;

/**
 * @Description:
 * @Date: Created on 14:06 2021/12/10
 */


public class GithubObservableCommand extends HystrixObservableCommand<GithubUser> {
    private final int userId;

    public GithubObservableCommand(int userId) {
        // super(HystrixCommandGroupKey.Factory.asKey("GithubGroup"));

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GithubGroup"))
                /**
                *  insert a command of this sort into a HystrixCommand constructor
                */
                .andCommandPropertiesDefaults(
                        /**
                        * next方法生产数据时的超时设置，超时则调用resumeWithFallback以及observer的onError
                        */
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)
                ));

        this.userId = userId;
    }

    /**
    * Hystrix invokes the request to the dependency here.
    */
    @Override
    protected Observable<GithubUser> construct() {
        /**
        * 构造参数：接口 void call(Subscriber<GithubUser> t);
        */
        return Observable.create(new SyncOnSubscribe<Integer, GithubUser>() {


            /**
            * Executed **once** when subscribed to by a subscriber(ie, observer)
             * 1. This method allows to initialize or start whatever you need to do for the Observable to start producing items.
             * 2. You can return anything here which will serve as the state. This comes into play in the next() method.
            */
            @Override
            protected Integer generateState() {
                return userId;
            }

            /**
            * The next(state, observer) method is called every time an item is requested from the Observable.
             * 1. When this method is called, your SyncOnSubscribe should produce exactly one item (potentially blocking)
             *    and call observer.onNext() once, or call observer.onComplete() or observer.onError() to indicate completion or error.
             * 2. So although this variable is called state it can be any object that you would require in your logic to produce the next value when next() is called.
             *    For example, state could be an InputStream you are reading items from.
            */
            @Override
            protected Integer next(Integer state, Observer<? super GithubUser> observer) {
                try {
                    Thread.sleep(1000);
                } catch (Exception exp) {
                    observer.onError(new Exception("githubClient error"));
                    return state + 1;
                }

                if (state - userId >= 3) {
                    observer.onCompleted();
                    return state + 1;
                }

                GithubClient githubClient = DemoApplicationContext.getBean(GithubClient.class);
                GithubUserResponse response = githubClient.user(state, null);
                GithubUser user = null;
                if (response != null) {
                    user = new GithubUser(response.getId(), response.getLogin(), response.getAvatar_url());
                } else {
                    observer.onError(new Exception("githubClient error"));
                    return state + 1;
                }

                observer.onNext(user);
                return state + 1;
            }

            /**
            * This onUnsubscribe(state) is a method you can optionally implement to clean up when the Observable is unsubscribed,
             *   and also receives the state again.
             * 1. This is where you could release resource used.
             *    For example, close the InputStream in the previous example.
            */
            @Override
            protected void onUnsubscribe(Integer state) {
                super.onUnsubscribe(state);
            }
        });
    }

    /**
    * 从isFallbackUserDefined方法可以知道，HystrixObservableCommand的fallback方法被重命名为了‘resumeWithFallback’
    */
    @Override
    protected Observable<GithubUser> resumeWithFallback() {
        System.out.println("GithubObservableCommand: resumeWithFallback");

        return null;
    }
}
