package com.example.anton.androidlibhomework.model.cache;

import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;
import com.example.anton.androidlibhomework.model.entity.realm.RealmRepository;
import com.example.anton.androidlibhomework.model.entity.realm.RealmUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;

public class RealmUserRepo implements CacheWorker {
    @Override
    public void putUser(GitHubUserModel user) {
            Realm realm = Realm.getDefaultInstance();
        final RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        realm.executeTransaction(innerRealm ->
        {
            if (realmUser == null) {
                    RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
            } else {
                realmUser.setAvatarUrl(user.getAvatarUrl());
            }
        });

            realm.close();
    }

    @Override
    public Observable<GitHubUserModel> getUser(String username) {
        return Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
            if (realmUser == null) {
                e.onError(new RuntimeException("No user in cacheRealm"));
            } else {
                e.onNext(new GitHubUserModel(realmUser.getLogin(), realmUser.getAvatarUrl()));
            }
            e.onComplete();
        });
    }

    @Override
    public void putUserRepos(GitHubUserModel user, List<GitHubUsersRepos> repositories) {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser == null) {
            realm.executeTransaction(innerRealm ->
            {
                    RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
                });
            }

        realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

        RealmUser finalRealmUser = realmUser;
            realm.executeTransaction(innerRealm -> {
                finalRealmUser.getRepositories().deleteAllFromRealm();
                for (GitHubUsersRepos repo : repositories) {
                    RealmRepository realmRepository = realm.createObject(RealmRepository.class, repo.getId());
                    realmRepository.setName(repo.getName());
                    finalRealmUser.getRepositories().add(realmRepository);
                }

            });
            realm.close();
    }

    @Override
    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user) {
        return Observable.create(e ->
        {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null) {
                e.onError(new RuntimeException("No user in cacheRealm"));
            } else {
                List<GitHubUsersRepos> repos = new ArrayList<>();
                for (RealmRepository realmRepository : realmUser.getRepositories()) {
                    repos.add(new GitHubUsersRepos(Integer.valueOf(realmRepository.getId()), realmRepository.getName()));
                }
                e.onNext(repos);
            }
            e.onComplete();
        });
        }
    }