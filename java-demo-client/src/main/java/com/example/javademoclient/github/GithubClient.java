package com.example.javademoclient.github;

import com.example.javademoclient.github.call.GithubService;
import com.example.javademodomain.github.GithubUserRequest;
import com.example.javademodomain.github.GithubUserResponse;
import com.example.javademoutility.log.LoggerProxy;
import okhttp3.OkHttpClient;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: Created on 17:12 2021/12/5
 */

@Component
public class GithubClient {
    public GithubUserResponse user(int id, GithubUserRequest githubUserRequest) {
        try {
            /**
            * OkHttpClient instance is going to take care of connecting to the server and
             *   the sending and retrieval of information.
            */
            /**
            * Default OkHttpClient causes java.io.InterruptedIOException in a write method:
             * okio.Okio$1.write(Okio.java:76)
            */
            // OkHttpClient httpClient = (new OkHttpClient.Builder()).build();

            OkHttpClient httpClient = (new OkHttpClient.Builder())
                    .connectTimeout(1000, TimeUnit.MILLISECONDS)
                    .readTimeout(1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(1000, TimeUnit.MILLISECONDS)
                    .build();

            /**
            * It's important to note that different factories serve different purposes.
             *So keep in mind that we can also use factories for XML, proto-buffers
             *   or even create one for a custom protocol.
             *For a list of already implemented factories, we can have a look at
             *   https://github.com/square/retrofit/tree/master/retrofit-converters
            */
            Retrofit retrofit = (new Retrofit.Builder())
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            /**
            * GithubService是一个接口，不能直接调用，必须通过retrofit创建出GithubService实例
            */
            GithubService service = retrofit.create(GithubService.class);
            Call<List<GithubUserResponse>> userCall = service.users();
            return userCall.execute().body().get(0);
        } catch (Exception exp) {
            LoggerProxy.console("GithubClient", "user", ""+ id, exp);
        }

        return null;
    }
}
