package com.example.anton.androidpoplibs;

import android.app.Application;

public class App extends Application {
    RxEventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus = new RxEventBus();
    }

    public RxEventBus getEventBus() {
        return eventBus;
    }
}
