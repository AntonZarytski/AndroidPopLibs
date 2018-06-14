package com.example.anton.androidlibhomework.views;

import com.arellomobile.mvp.MvpView;

public interface MainView extends MvpView {
    void init();

    void showAvatar(String avatarUrl);

    void showError(String message);

    void setUsername(String username);

    void showLoading();

    void hideLoading();

    void updateRepoList();

    void checkPermissions();
}
