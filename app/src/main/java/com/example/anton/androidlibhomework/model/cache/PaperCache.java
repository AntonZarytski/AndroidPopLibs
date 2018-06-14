package com.example.anton.androidlibhomework.model.cache;

import com.example.anton.androidlibhomework.model.common.Utils;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;

public class PaperCache implements CacheWorker {
    public static final String USER_BOOK = "users";
    public static final String REPOS_BOOK = "repositories";

    @Override
    public void putUser(GitHubUserModel user) {
        Paper.book(USER_BOOK).write(user.getLogin(), user);

    }

    @Override
    public Observable<GitHubUserModel> getUser(String username) {
        if (!Paper.book(USER_BOOK).contains(username)) {
            return Observable.error(new RuntimeException("No such user in cacheRealm: " + username));
        }
        return Observable.fromCallable(() -> Paper.book(USER_BOOK).read(username));
    }

    @Override
    public void putUserRepos(GitHubUserModel user, List<GitHubUsersRepos> repositories) {
        String md5 = Utils.MD5(user.getReposUrl());
        Paper.book(REPOS_BOOK).write(md5, repositories);
    }

    @Override
    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user) {
        String md5 = Utils.MD5(user.getReposUrl());
        if (!Paper.book(REPOS_BOOK).contains(md5)) {
            return Observable.error(new RuntimeException("No such repositiries list in cacheRealm: " + user.getReposUrl()));
        }
        return Observable.fromCallable(() -> Paper.book(USER_BOOK).read(md5));
    }

}
