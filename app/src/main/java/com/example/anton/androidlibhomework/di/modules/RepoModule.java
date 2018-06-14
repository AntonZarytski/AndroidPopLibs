package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.api.ApiService;
import com.example.anton.androidlibhomework.model.cache.CacheWorker;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {
    @Provides
    public UsersRepo usersRepo(CacheWorker cache, ApiService api) {
        return new UsersRepo(cache, api);
    }
}
