package com.example.anton.androidlibhomework.model.cache;

import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.reactivex.Observable;

public interface CacheWorker {
    void putUser(GitHubUserModel user);

    Observable<GitHubUserModel> getUser(String username);

    void putUserRepos(GitHubUserModel user, List<GitHubUsersRepos> repositories);

    Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user);
}
