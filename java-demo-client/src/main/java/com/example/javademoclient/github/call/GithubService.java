package com.example.javademoclient.github.call;

import com.example.javademodomain.github.GithubUserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * Document on:
 * retrofit2: https://square.github.io/retrofit/
 * okhttp3: https://square.github.io/okhttp/3.x/okhttp/
 * 1. Retrofit turns your HTTP API into a Java interface.
 *    It works by modeling over a base URL and by making interfaces return the entity from the REST endpoint.
 * 2. Annotations on the interface methods and its parameters indicate how a request will be handled.
 * @Date: Created on 16:45 2021/12/5
 */


public interface GithubService {
    /**
    * get Standard Deviation of a Fund
    */
    @GET("/users/{id}")
    @Headers({
            "User-Agent: Retrofit-Sample-App"
    })
    Call<GithubUserResponse> user(@Path("id") int id);

    @GET("/users")
    @Headers({
            "User-Agent: Retrofit-Sample-App"
    })
    Call<List<GithubUserResponse>> users();
}
