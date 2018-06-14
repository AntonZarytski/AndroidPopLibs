package com.example.anton.androidlibhomework;

import android.app.Application;

import com.example.anton.androidlibhomework.di.AppComponent;
import com.example.anton.androidlibhomework.di.DaggerAppComponent;
import com.example.anton.androidlibhomework.di.modules.AppModule;

import io.paperdb.Paper;
import io.realm.Realm;

//TODO почему такая ошибка здесь?
//D:\MyLearningRepo\AndroidPopularLibs\app\src\main\java\com\example\anton\androidlibhomework\App.java:6: error: cannot find symbol
//import com.example.anton.androidlibhomework.di.DaggerAppComponent;
//                                              ^
//                                                      symbol:   class DaggerAppComponent
//  location: package com.example.anton.androidlibhomework.di
//        Note: Processing class CachedImage
//Note: Processing class RealmRepository
//Note: Processing class RealmUser
//Note: Creating DefaultRealmModule
//        Note: [1] Wrote GeneratedAppGlideModule with: []
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

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
