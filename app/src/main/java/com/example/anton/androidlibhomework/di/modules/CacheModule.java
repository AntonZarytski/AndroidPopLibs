package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.cache.ICache;
import com.example.anton.androidlibhomework.model.cache.RealmCache;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class CacheModule {


    @Named("realm")
    @Provides
    public ICache cacheRealm() {
        return new RealmCache();
    }
}
