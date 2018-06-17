package com.example.anton.androidlibhomework.di.modules;

import com.example.anton.androidlibhomework.model.repo.UsersRepo;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;

@Module
public class TestRepoModule {
    @Provides
    public UsersRepo usersRepo() {
        return Mockito.mock(UsersRepo.class);
    }
}
