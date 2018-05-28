package com.example.anton.androidpoplibs;

import io.reactivex.Observable;

public class TextModel {
    private String text;

    public Observable<String> getText(final String text) {
        return Observable.just(text);
    }

    public void setText(String text) {
        this.text = text;
    }
}
