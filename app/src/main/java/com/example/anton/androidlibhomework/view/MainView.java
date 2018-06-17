package com.example.anton.androidlibhomework.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    void showAvatar(String avatarUrl);

    void showError(String message);

    void setUsername(String username);

    void showLoading();

    void hideLoading();

    void init();

    void updateRepoList();
}
