package com.example.anton.androidlibhomework;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServiceGetRepoitories {
    @GET("users/{user}/repos")
    Observable<List<GitHubUsersRepos>> loadRepos(@Path("user") String user);
}
