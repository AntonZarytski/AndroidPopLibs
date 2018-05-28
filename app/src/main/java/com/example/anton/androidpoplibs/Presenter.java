package com.example.anton.androidpoplibs;


import io.reactivex.Observable;

public class Presenter {
    private TextModel model;

    public Presenter() {
        model = new TextModel();
    }

    Observable<String> setText(String s) {
        return model.getText(s);
    }
}
