package com.example.anton.androidlibhomework.model.api;

import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("users/{userName}")
    Observable<User> getUser(@Path("userName") String userName);

    @GET("users/{user}/repos")
    Observable<List<Repository>> getUserRepos(@Path("user") String userName);
}
