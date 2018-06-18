package com.example.anton.androidlibhomework;

import android.content.Context;

import com.example.anton.androidlibhomework.model.cache.RealmCache;
import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;
import com.example.anton.androidlibhomework.model.entity.realm.RealmRepository;
import com.example.anton.androidlibhomework.model.entity.realm.RealmUser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.realm.Realm;

import static junit.framework.Assert.assertEquals;

public class CahceInstrumentedTest {
    private static RealmCache realmCache;
    @Mock
    private static Realm realm;
    @Mock
    Context fakeContext;
    @Inject
    App app;

    @BeforeClass
    public static void setupClass() throws IOException {
        realmCache = new RealmCache();
        realm = Mockito.spy(Realm.class);
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Realm realm = Realm.getDefaultInstance();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getUser(String userName) {
        TestObserver<User> observer = new TestObserver<>();
        Observable<User> userObservable = realmCache.getUser(userName);
        userObservable.subscribe(observer);
        observer.awaitTerminalEvent();
        assertEquals(observer.values().get(0).getLogin(), userName);
    }

    @Test
    public void getRepos(User user) {
        TestObserver<List<Repository>> observer = new TestObserver<>();
        Observable<List<Repository>> listObservable = realmCache.getUserRepos(user);
        listObservable.subscribe(observer);
        observer.awaitTerminalEvent();
        for (int i = 0; i < observer.values().size(); i++) {
            assertEquals(observer.values().get(0).get(i), user.getRepos().get(i));
        }
    }

    @Test
    public void putUser(User user) {
        TestObserver<User> observer = new TestObserver<>();
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
        realmCache.getUser(user.getLogin()).subscribe(observer);
        observer.awaitTerminalEvent();
        assertEquals(observer.values().get(0).getLogin(), user.getLogin());
        realm.close();

    }

    @Test
    public void putRepos(User user, List<Repository> repositories) {
        TestObserver<List<Repository>> observer = new TestObserver<>();
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
            for (Repository repo : repositories) {
                RealmRepository realmRepository = realm.createObject(RealmRepository.class, repo.getId());
                realmRepository.setName(repo.getName());
                finalRealmUser.getRepositories().add(realmRepository);
            }

        });
        realmCache.getUserRepos(user).subscribe(observer);
        observer.awaitTerminalEvent();
        for (int i = 0; i < observer.values().size(); i++) {
            assertEquals(observer.values().get(0).get(i), user.getRepos().get(i));
        }
        realm.close();
    }
}