package com.example.anton.androidlibhomework.model.repo;

import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.reactivex.Observable;

public interface CacheWorker {
    Observable<GitHubUserModel> getUser(final String username);

    Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user);

    Observable<GitHubUserModel> downloadAndPutUser(String username);

    Observable<List<GitHubUsersRepos>> downloadAndPutRepos(GitHubUserModel user);
}
