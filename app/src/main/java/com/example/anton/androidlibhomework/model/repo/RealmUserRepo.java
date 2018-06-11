package com.example.anton.androidlibhomework.model.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.anton.androidlibhomework.model.api.ApiHolder;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.entity.GitHubUsersRepos;
import com.example.anton.androidlibhomework.model.entity.realm.RealmRepository;
import com.example.anton.androidlibhomework.model.entity.realm.RealmUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class RealmUserRepo implements CacheWorker {
    public static final String USER_BOOK = "users";
    public static final String REPOS_BOOK = "repositories";
    private final String pathToSaveImage = Environment.getExternalStorageDirectory().toString();


    @Override
    public Observable<GitHubUserModel> getUser(final String username) {
        return Observable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class)
                    .equalTo("login", username)
                    .findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache " + username));
            } else {
                emitter.onNext(new GitHubUserModel(realmUser.getLogin(), realmUser.getImageUrl(), realmUser.getRepoUrl()));
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<GitHubUsersRepos>> getUserRepos(GitHubUserModel model) {
        return Observable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", model.getLogin()).findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache: " + model.getLogin()));
            } else {
                List<GitHubUsersRepos> repositories = new ArrayList<>();
                for (RealmRepository relmRepository : realmUser.getRepos()) {
                    repositories.add(new GitHubUsersRepos(Integer.valueOf(relmRepository.getId()), relmRepository.getName()));
                }
                emitter.onNext(repositories);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<GitHubUserModel> downloadAndPutUser(String username) {
        return ApiHolder.getApi().loadUser(username).subscribeOn(Schedulers.io()).map(user -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class)
                    .equalTo("login", username)
                    .findFirst();
            if (realmUser == null) {
                //TODO все операции выполняются  транзакциями
                realm.executeTransaction(innerRealm -> {
                    //TODO обязательно указываем поле которое автоинкремент(если оно есть)
                    RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                    newRealmUser.setImageUrl(user.getAvatarUrl());
                });
            } else {
                realm.executeTransaction(inneRealm -> {
                    realmUser.setImageUrl(user.getAvatarUrl());
                });
            }
            realm.close();
            return user;
        });
    }

    @Override
    public Observable<List<GitHubUsersRepos>> downloadAndPutRepos(GitHubUserModel user) {
        return ApiHolder.getApi().loadRepos(user.getReposUrl()).subscribeOn(Schedulers.io()).map(gitHubUsersRepos -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null) {
                realm.executeTransaction(innerRealm -> {
                    RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                    newRealmUser.setImageUrl(user.getAvatarUrl());
                });
            }
            final RealmUser finalRealmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm innerRealm) {
                    finalRealmUser.getRepos().deleteAllFromRealm();
                    for (GitHubUsersRepos repository : gitHubUsersRepos) {
                        RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, String.valueOf(repository.getId()));
                        realmRepository.setName(repository.getName());
                        finalRealmUser.getRepos().add(realmRepository);
                    }
                }
            });
            realm.close();
            return gitHubUsersRepos;
        });
    }

    public Observable<List<GitHubUsersRepos>> getRepos(GitHubUserModel user) {
        return ApiHolder.getApi().loadRepos(user.getReposUrl()).subscribeOn(Schedulers.io()).map(gitHubUsersRepos -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null) {
                realm.executeTransaction(innerRealm -> {
                    RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                    newRealmUser.setImageUrl(user.getAvatarUrl());
                });
            }
            final RealmUser finalRealmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            realm.executeTransaction(innerRealm -> {
                finalRealmUser.getRepos().deleteAllFromRealm();
                for (GitHubUsersRepos repository : gitHubUsersRepos) {
                    RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                    realmRepository.setName(repository.getName());
                    finalRealmUser.getRepos().add(realmRepository);
                }
            });
            realm.close();
            return gitHubUsersRepos;
        });
    }

    Bitmap loadImage(String filePath) {
        InputStream fIn = null;
        try {
            File file = new File(filePath);
            fIn = new FileInputStream(file);
            Bitmap image = BitmapFactory.decodeStream(fIn);
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    String saveImage(Bitmap image) {
        OutputStream fOut = null;
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        try {
            File file = new File(pathToSaveImage, Integer.toString(time.getYear()) +
                    Integer.toString(time.getMonth()) + Integer.toString(time.getDay()) +
                    Integer.toString(time.getHours()) + Integer.toString(time.getMinutes()) +
                    Integer.toString(time.getSeconds()) + ".jpg");
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file.toString();
            // TODO регистрация в фотоальбоме. Можно ли в GlideImageLoader передать context? или как правильно сделать?
//            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            return "";
        }
    }
}
