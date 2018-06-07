package com.example.anton.androidlibhomework.model.repo;

import com.example.anton.androidlibhomework.model.api.ApiHolder;
import com.example.anton.androidlibhomework.model.common.Utils;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PaperUsersRepo implements CacheWorker {
    public static final String USER_BOOK = "users";
    public static final String REPOS_BOOK = "repositories";

    public Observable<GitHubUserModel> getUser(final String username) {
        if (!Paper.book(USER_BOOK).contains(username)) {
            return Observable.error(new RuntimeException("No such user in cache: " + username));
        }
        return Observable.fromCallable(() -> Paper.book(USER_BOOK).read(username));
    }

    @Override
    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user) {
        String md5 = Utils.MD5(user.getReposUrl());
        if (!Paper.book(REPOS_BOOK).contains(md5)) {
            return Observable.error(new RuntimeException("No such repositiries list in cache: " + user.getReposUrl()));
        }
        return Observable.fromCallable(() -> Paper.book(USER_BOOK).read(md5));
    }

    @Override
    public Observable<GitHubUserModel> downloadAndPutUser(String username) {
        return ApiHolder.getApi().loadUser(username).subscribeOn(Schedulers.io()).map(usertemp -> {
            Paper.book(USER_BOOK).write(usertemp.getLogin(), usertemp);
            return usertemp;
        });
    }

    @Override
    public Observable<List<GitHubUsersRepos>> downloadAndPutRepos(GitHubUserModel user) {
        String md5 = Utils.MD5(user.getReposUrl());
        return ApiHolder.getApi().loadRepos(user.getReposUrl()).subscribeOn(Schedulers.io()).map(repositories -> {
            Paper.book(REPOS_BOOK).write(md5, repositories);
            return repositories;
        });
    }

}