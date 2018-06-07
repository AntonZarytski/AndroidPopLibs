package com.example.anton.androidlibhomework.model.api;

import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
    @GET("users/{user}")
    Observable<GitHubUserModel> loadUser(@Path("user") String user);

    @GET
    Observable<List<GitHubUsersRepos>> loadRepos(@Url String url);
}
