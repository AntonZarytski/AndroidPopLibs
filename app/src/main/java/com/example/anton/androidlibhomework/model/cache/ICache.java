package com.example.anton.androidlibhomework.model.cache;

import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;

import java.util.List;

import io.reactivex.Observable;


public interface ICache {
    void putUser(User user);

    Observable<User> getUser(String username);

    void putUserRepos(User user, List<Repository> repositories);

    Observable<List<Repository>> getUserRepos(User user);
}
