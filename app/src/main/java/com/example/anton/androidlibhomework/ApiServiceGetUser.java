package com.example.anton.androidlibhomework;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServiceGetUser {
    @GET("users/{user}")
    Observable<GitHubUserModel> loadUser(@Path("user") String user);
}
