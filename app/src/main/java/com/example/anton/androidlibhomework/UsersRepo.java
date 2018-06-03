package com.example.anton.androidlibhomework;

import java.util.List;

import io.reactivex.Observable;

public class UsersRepo {
    public Observable<GitHubUserModel> getUser(String username) {
        return ApiHolder.getApiUser().loadUser(username);
    }

    public Observable<List<GitHubUsersRepos>> getRepos(String username) {
        return ApiHolder.getApiRepositories().loadRepos(username);
    }

}