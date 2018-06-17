package com.example.anton.androidlibhomework.di;

import com.example.anton.androidlibhomework.di.modules.TestRepoModule;
import com.example.anton.androidlibhomework.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestRepoModule.class})
public interface TestComponent {
    void inject(MainPresenter presenter);
}
