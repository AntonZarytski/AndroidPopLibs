package com.example.anton.androidlibhomework.model.image;

public interface IimageLoader<T> {
    void loadInto(String url, T container);
}
