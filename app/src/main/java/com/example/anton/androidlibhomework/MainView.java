package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;


@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    File getFile();
    File writeFile();
}
