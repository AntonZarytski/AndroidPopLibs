package com.example.anton.androidlibhomework.model.repo;

import com.example.anton.androidlibhomework.model.api.ApiService;
import com.example.anton.androidlibhomework.model.cache.CacheWorker;
import com.example.anton.androidlibhomework.model.common.NetworkStatus;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class UsersRepo {
    CacheWorker cache;
    ApiService api;

    public UsersRepo(CacheWorker cache, ApiService api) {
        this.cache = cache;
        this.api = api;
    }

    public Observable<GitHubUserModel> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return api.loadUser(username).subscribeOn(Schedulers.io()).map(user -> {
                cache.putUser(user);
                return user;
            });
        } else {
            return cache.getUser(username);
        }
    }

    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user) {
        if (NetworkStatus.isOnline()) {
            return api.loadRepos(user.getReposUrl()).subscribeOn(Schedulers.io()).map(userRepositories -> {
                cache.putUserRepos(user, userRepositories);
                return userRepositories;
            });
        } else {
            return cache.getUserRepos(user);
        }
    }
}
