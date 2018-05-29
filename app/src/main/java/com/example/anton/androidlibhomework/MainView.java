package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import io.reactivex.Observable;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    void setThreeValue(Observable<Integer> observable);

    void setTwoValue(Observable<Integer> observable);

    void setOneValue(Observable<Integer> observable);
}
