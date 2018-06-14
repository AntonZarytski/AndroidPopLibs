package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.cache.CacheWorker;
import com.example.anton.androidlibhomework.model.cache.PaperCache;
import com.example.anton.androidlibhomework.model.cache.RealmUserRepo;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {
    @Provides
    @Named("ForRealm")
    public CacheWorker cacheRealm() {
        return new RealmUserRepo();
    }

    @Provides
    @Named("ForPaper")
    public CacheWorker cachePaper() {
        return new PaperCache();
    }
}
