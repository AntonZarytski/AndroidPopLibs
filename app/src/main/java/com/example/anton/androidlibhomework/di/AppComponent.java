package com.example.anton.androidlibhomework.di;

import com.example.anton.androidlibhomework.di.modules.AppModule;
import com.example.anton.androidlibhomework.di.modules.RepoModule;
import com.example.anton.androidlibhomework.presenters.MainPresenter;
import com.example.anton.androidlibhomework.views.MainActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = {AppModule.class, RepoModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    @Provides
    @Named("ForRealm")
    void inject(MainPresenter activity);
}
