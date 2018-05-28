package com.example.anton.androidpoplibs;

import io.reactivex.Observable;

public class TextModel {
    private String text;

    public Observable<String> setText(final String s) {
        text = s;
        return Observable.just(text);
    }
}
