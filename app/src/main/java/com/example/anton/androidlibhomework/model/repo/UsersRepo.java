package com.example.anton.androidlibhomework.model.repo;

import com.example.anton.androidlibhomework.model.api.ApiHolder;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

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