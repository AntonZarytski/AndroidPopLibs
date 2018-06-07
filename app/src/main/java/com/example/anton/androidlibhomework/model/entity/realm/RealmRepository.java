package com.example.anton.androidlibhomework.model.entity.realm;

import io.realm.RealmObject;

public class RealmRepository extends RealmObject {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
