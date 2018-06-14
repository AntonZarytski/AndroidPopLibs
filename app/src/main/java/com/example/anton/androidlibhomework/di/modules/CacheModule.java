package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.cache.CacheWorker;
import com.example.anton.androidlibhomework.model.cache.RealmUserRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {
    @Provides
    public CacheWorker cache() {
        return new RealmUserRepo();
    }
}
