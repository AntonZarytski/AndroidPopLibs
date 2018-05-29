package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Observable;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private CounterModel model;


    public MainPresenter() {
        this.model = new CounterModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public Observable<Integer> calcValue(int index) {
        model.setAt(index, model.getAt(index) + 1);
        return model.getObservable(index);
    }

    public void onButtonOneClick() {
        getViewState().setOneValue(calcValue(0));
    }

    public void onButtonTwoClick() {
        getViewState().setTwoValue(calcValue(1));
    }

    public void onButtonThreeClick() {
        getViewState().setThreeValue(calcValue(2));
    }

}
