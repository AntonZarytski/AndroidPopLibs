package com.example.anton.androidlibhomework.di;

import com.example.anton.androidlibhomework.di.modules.AppModule;
import com.example.anton.androidlibhomework.di.modules.ImageLoaderModule;
import com.example.anton.androidlibhomework.di.modules.RepoModule;
import com.example.anton.androidlibhomework.presenter.MainPresenter;
import com.example.anton.androidlibhomework.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RepoModule.class, ImageLoaderModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(MainPresenter presenter);
}
