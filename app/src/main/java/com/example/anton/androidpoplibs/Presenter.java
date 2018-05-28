package com.example.anton.androidpoplibs;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Observable;

@InjectViewState
public class Presenter extends MvpPresenter<ActivityMain> {
    private TextModel model;
    private ActivityMain activityMain;
    private Observable<String> observable;

    public Presenter() {
        model = new TextModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    Observable<String> getObservable() {
        return observable;
    }

    public void getText(String text) {
        getViewState().setText(text);
    }

    public void CharIsEntered(String s) {
        observable = model.setText(s);
    }
}
