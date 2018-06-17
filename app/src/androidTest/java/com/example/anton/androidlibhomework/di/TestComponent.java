package com.example.anton.androidlibhomework.di;


import com.example.anton.androidlibhomework.UserRepoInstrumentedTest;
import com.example.anton.androidlibhomework.di.modules.RepoModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepoModule.class})
public interface TestComponent {
    void inject(UserRepoInstrumentedTest test);
}
