package com.example.anton.androidlibhomework.model.entity.realm;

import io.realm.RealmObject;

public class CachedImage extends RealmObject {
    String url;
    String path;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
