package com.example.anton.androidlibhomework;

import android.app.Application;

import io.paperdb.Paper;
import io.realm.Realm;
import timber.log.Timber;

public class App extends Application {
    private static App instance = null;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Paper.init(this);
//        Configuration dbConfiguration = new Configuration.Builder(this)
//                .setDatabaseName("MyDb.db")
//                .addModelClass(AAUser.class)
//                .addModelClass(AARepository.class)
//                .create();

//        ActiveAndroid.initialize(this);
        Realm.init(this);

    }
}
