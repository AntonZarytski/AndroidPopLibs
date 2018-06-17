package com.example.anton.androidlibhomework.model.repo;


import com.example.anton.androidlibhomework.NetworkStatus;
import com.example.anton.androidlibhomework.model.api.ApiService;
import com.example.anton.androidlibhomework.model.cache.ICache;
import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class UsersRepo {
    ApiService api;
    ICache cache;

    public UsersRepo(ApiService api, ICache cache) {
        this.cache = cache;
        this.api = api;
    }

    public Observable<User> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return api.getUser(username).subscribeOn(Schedulers.io()).map(user ->
            {
                cache.putUser(user);
                return user;
            });
        } else {
            return cache.getUser(username).subscribeOn(Schedulers.io());
        }
    }

    public Observable<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) {
            return api.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io()).map(repos ->
            {
                cache.putUserRepos(user, repos);
                return repos;
            });
        } else {
            return cache.getUserRepos(user).subscribeOn(Schedulers.io());
        }
    }
}
