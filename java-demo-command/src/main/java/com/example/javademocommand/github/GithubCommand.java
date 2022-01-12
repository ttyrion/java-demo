package com.example.javademocommand.github;

import com.example.javademoclient.github.GithubClient;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.bean.DemoApplicationContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * @Description: Hystrix: https://github.com/Netflix/Hystrix/wiki/
 * @Date: Created on 10:56 2021/12/8
 * 99%的需求都是通过HystrixCommand完成，极少数（需要返回多个操作结果）的情况下，使用HystrixObservableCommand
 * 两者都是继承自AbstractCommand，AbstractCommand完成了绝大部分逻辑。
 * 1. 只有HystrixCommand实现了HystrixExecutable接口，也就是说，只有HystrixCommand支持同步和异步两种执行方式：
 *     -execute(): 同步执行。从依赖的服务返回单个结果，或是在发生错误的时候抛出异常。
 *     -queue(): 异步执行。返回一个Future，其中包含了服务执行结束时要返回的单个结果。
 *     其实，execute()方法内部也是通过queue()方法实现：queue().get()
 * 2. HystrixObservableCommand 同样也支持两种执行方式：
 *    -observe(): 返回Obervable对象，它代表了操作的多个结果，他是一个HotObservable
 *    -toObservable(): 同样返回Observable对象，也代表了多个结果，但它返回的是一个Cold Observable。
 */
public class GithubCommand extends HystrixCommand<GithubUser> {
    private final int userId;

    public GithubCommand(int userId) {
        /**
        * Hystrix uses the command group key to group together commands such as for reporting, alerting, dashboards, or team/library ownership.
         * By default, Hystrix uses this to define the command thread-pool unless a separate one is defined.
         *
         * The thread-pool key represents a HystrixThreadPool for monitoring, metrics publishing, caching, and other such uses.
         * A HystrixCommand is associated with a single HystrixThreadPool as retrieved by the HystrixThreadPoolKey injected into it,
         *    or it defaults to one created using the HystrixCommandGroupKey it is created with.
         *
         * 使用HystrixThreadPoolKey 而不是
        */
        // super(HystrixCommandGroupKey.Factory.asKey("GithubGroup"));
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GithubGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GithubUser"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GithubGroupPool")));
        this.userId = userId;
    }

    @Override
    protected GithubUser run() throws Exception {
        GithubClient githubClient = DemoApplicationContext.getBean(GithubClient.class);
        GithubUserResponse response = githubClient.user(userId, null);
        if (response != null) {
            return new GithubUser(response.getId(), response.getLogin(), response.getAvatar_url());
        }

        return null;
    }

    /**
    * 默认是抛出异常："No fallback available."
    */
    @Override
    protected GithubUser getFallback() {
        return new GithubUser();
    }
}
