package com.example.anton.androidlibhomework.model.entity.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmUser extends RealmObject {
    @PrimaryKey
    String login;
    String imageUrl;
    String repoUrl;
    RealmList<RealmRepository> repos = new RealmList<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public RealmList<RealmRepository> getRepos() {
        return repos;
    }

    public void setRepos(RealmList<RealmRepository> repos) {
        this.repos = repos;
    }
}
