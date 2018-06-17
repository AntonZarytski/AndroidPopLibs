package com.example.anton.androidlibhomework.model.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    String avatarUrl;
    String login;
    List<Repository> repos = new ArrayList<>();

    public User(String login, String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.login = login;
        this.repos = new ArrayList<>();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Repository> getRepos() {
        return repos;
    }

    public void setRepos(List<Repository> repos) {
        this.repos = repos;
    }
}
