package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.api.ApiService;
import com.example.anton.androidlibhomework.model.cache.ICache;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {
    @Singleton
    @Provides
    public UsersRepo usersRepo(ApiService apiService, @Named("realm") ICache cache) {
        return new UsersRepo(apiService, cache);
    }
}
