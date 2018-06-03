package com.example.anton.androidlibhomework;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHolder {
    public static final String BASE_URL = "https://api.github.com";

    private static ApiHolder instance = new ApiHolder();
    private ApiServiceGetUser apiUsers;
    private ApiServiceGetRepoitories apiRepos;

    private ApiHolder() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        apiUsers = getRetrofit(gson)
                .create(ApiServiceGetUser.class);
        apiRepos = getRetrofit(gson)
                .create(ApiServiceGetRepoitories.class);
    }

    public static ApiHolder getInstance() {
        if (instance == null) {
            instance = new ApiHolder();
        }
        return instance;
    }

    public static ApiServiceGetUser getApiUser() {
        return getInstance().apiUsers;
    }

    public static ApiServiceGetRepoitories getApiRepositories() {
        return getInstance().apiRepos;
    }

    private Retrofit getRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}