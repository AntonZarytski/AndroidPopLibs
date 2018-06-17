package com.example.anton.androidlibhomework;

import android.app.Application;

import com.example.anton.androidlibhomework.di.AppComponent;
import com.example.anton.androidlibhomework.di.DaggerAppComponent;
import com.example.anton.androidlibhomework.di.modules.AppModule;

import io.paperdb.Paper;
import io.realm.Realm;
import timber.log.Timber;

public class App extends Application {
    private static App instance;

    private AppComponent appComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Paper.init(this);
        Realm.init(this);
        Timber.plant(new Timber.DebugTree());
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
