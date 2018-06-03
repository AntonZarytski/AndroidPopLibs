package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.MvpView;

public interface MainView extends MvpView {
    void setUsernameText(String username);

    void loadImage(String url);

    void setId(String id);

    void setRepos(String repoName, int i);

    void setProgresVisibility(Boolean bolean);
}
