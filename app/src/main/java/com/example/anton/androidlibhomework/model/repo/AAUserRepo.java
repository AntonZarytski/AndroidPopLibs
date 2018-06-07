package com.example.anton.androidlibhomework.model.repo;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.anton.androidlibhomework.model.api.ApiHolder;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;
import com.example.anton.androidlibhomework.model.entity.activeAndroid.AARepository;
import com.example.anton.androidlibhomework.model.entity.activeAndroid.AAUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class AAUserRepo implements CacheWorker {

    public Observable<GitHubUserModel> getUser(String username) {
        return Observable.create(emitter -> {
            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", username)
                    .executeSingle();

            if (aaUser == null) {
                emitter.onError(new RuntimeException("No such user in cache: " + username));
            } else {
                emitter.onNext(new GitHubUserModel(aaUser.login, aaUser.avatarUrl, aaUser.reposUrl));
                emitter.onComplete();
            }

        });
    }

    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel user) {
        return Observable.create(emitter -> {
            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", user.getLogin())
                    .executeSingle();


            if (aaUser == null) {
                emitter.onError(new RuntimeException("No such user in cache: " + user.getLogin()));
            } else {
                List<GitHubUsersRepos> repos = new ArrayList<>();
                for (AARepository aaRepository : aaUser.repositories()) {
                    repos.add(new GitHubUsersRepos(Integer.valueOf(aaRepository.id), aaRepository.name));
                }
                emitter.onNext(repos);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<GitHubUserModel> downloadAndPutUser(String username) {
        return ApiHolder.getApi().loadUser(username).subscribeOn(Schedulers.io()).map(user -> {

            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", username)
                    .executeSingle();

            if (aaUser == null) {
                aaUser = new AAUser();
                aaUser.login = user.getLogin();
            }

            aaUser.avatarUrl = user.getAvatarUrl();
            aaUser.save();
            return user;
        });
    }

    @Override
    public Observable<List<GitHubUsersRepos>> downloadAndPutRepos(GitHubUserModel user) {
        return ApiHolder.getApi().loadRepos(user.getReposUrl()).subscribeOn(Schedulers.io()).map(repositories -> {

            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", user.getLogin())
                    .executeSingle();

            if (aaUser == null) {
                aaUser = new AAUser();
                aaUser.login = user.getLogin();
                aaUser.avatarUrl = user.getAvatarUrl();
                aaUser.save();
            }

            new Delete().from(AARepository.class).where("user = ?", aaUser.getId()).execute();

            ActiveAndroid.beginTransaction();
            try {
                for (GitHubUsersRepos repository : repositories) {
                    AARepository aaRepository = new AARepository();
                    aaRepository.id = String.valueOf(repository.getId());
                    aaRepository.name = repository.getName();
                    aaRepository.user = aaUser;
                    aaRepository.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }


            return repositories;
        });
    }
}