package com.example.javademocommand.github;

import com.example.javademoclient.github.GithubClient;
import com.example.javademodomain.github.GithubUser;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.bean.DemoApplicationContext;
import com.netflix.hystrix.*;

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

                /**
                * 线程池名称
                */
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GithubGroupPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withMaxQueueSize(100))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withKeepAliveTimeMinutes(1))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withQueueSizeRejectionThreshold(30)));
        this.userId = userId;
    }

    /**
     * Hystrix invokes the request to the dependency here.
     * 如果HystrixCommand没有抛出任何异常且返回了一个响应，那Hystrix将在输出一些日志以及上报一些指标之后返回此响应。
     * 如以此run方法为例，Hystrix返回一个发送单个响应的Observable对象且发出一个onCompleted通知。
     * 如以construct方法为例，Hystrix返回construct()方法返回的那个Observable对象。
     */
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
     * 什么情况下会调用Fallback？
     * 1. In step 4, if the circuit is open (or “tripped” or "short circuit"), Hystrix will not execute the command
     *    but immediately get the fallback.
     * 2. In step 5, if the thread-pool and queue that are associated with the command are full, Hystrix will not execute the command
     *    but immediately get the fallback.
     * 3. In step 6, if run() or construct() method exceeds the command（ie, the hystrix command）’s timeout value,
     *    the thread will throw a TimeoutException（ or a separate timer thread will, if the command itself is
     *    not running in its own thread）.
     *    这种情况下，Hystrix接着就会返回Fallback且忽略run() or construct()方法最终的返回值（如果此方法的执行没有被中断或者取消）。
     *    注意，这里也就是说，即便是Hystrix已经返回（超时，fallback），对第三方依赖的请求仍然可能会继续。Hystrix所能做的只是抛出一个
     *    InterruptedException。如果封装在HystrixCommand中的任务不对此异常进行处理，那么Hystrix线程池中的线程将继续工作，尽管此时
     *    客户端代码已经收到了一个TimeoutException。
     *
     *    Note: 因此，尽管Hystrix已经成功减轻了负载，上述行为仍可能使得Hystrix线程池的任务饱和。多数Java HTTP客户端并不处理InterruptedException。
     *    所以我们应该确保正确地配置一下所调用的HTTP库的r/w超时值。
     *
    */
    @Override
    protected GithubUser getFallback() {
        return new GithubUser();
    }
}
