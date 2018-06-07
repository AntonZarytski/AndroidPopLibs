package com.example.anton.androidlibhomework.model.entity.activeAndroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "users")
public class AAUser extends Model {
    @Column(name = "login")
    public String login;

    @Column(name = "avatar_url")
    public String avatarUrl;

    @Column(name = "repos_url")
    public String reposUrl;

    public List<AARepository> repositories() {
        return getMany(AARepository.class, "user");
    }
}

