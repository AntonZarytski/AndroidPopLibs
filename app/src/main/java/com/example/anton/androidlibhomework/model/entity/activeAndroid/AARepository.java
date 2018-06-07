package com.example.anton.androidlibhomework.model.entity.activeAndroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "repositories")
public class AARepository extends Model {
    @Column(name = "github_id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "user")
    public AAUser user;

    @Column(name = "self_url")
    public String selfUrl;
}

