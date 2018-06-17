package com.example.anton.androidlibhomework;

import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;
import com.example.anton.androidlibhomework.model.entity.realm.RealmRepository;
import com.example.anton.androidlibhomework.model.entity.realm.RealmUser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.realm.Realm;

import static junit.framework.Assert.assertEquals;

//TODO MOCKITO отказывается работать в пакете с интрументтесты
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
public class CahceInstrumentedTest {
    //    @Mock
    private static Realm realm;
    @Inject
    App app;

    @BeforeClass
    public static void setupClass() throws IOException {
//        MockitoAnnotations.initMocks(this)
        realm = Realm.getDefaultInstance();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        realm.close();
    }

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void getUser(String userName) {
        TestObserver<User> observer = new TestObserver<>();
        Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", userName).findFirst();
            if (realmUser == null) {
                e.onError(new RuntimeException("No user in cache"));
            } else {
                e.onNext(new User(realmUser.getLogin(), realmUser.getAvatarUrl()));
            }
            e.onComplete();
        }).subscribe((Consumer<? super Object>) observer);
        observer.awaitTerminalEvent();
        assertEquals(observer.values().get(0).getLogin(), userName);
    }

    @Test
    public void getRepos(User user) {
        TestObserver<List<Repository>> observer = new TestObserver<>();
        Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null) {
                e.onError(new RuntimeException("No user in cache"));
            } else {
                List<Repository> repos = new ArrayList<>();
                for (RealmRepository realmRepository : realmUser.getRepositories()) {
                    repos.add(new Repository(realmRepository.getId(), realmRepository.getName()));
                }
                e.onNext(repos);
            }
            e.onComplete();
        }).subscribe((Consumer<? super Object>) observer);
        observer.awaitTerminalEvent();
        for (int i = 0; i < observer.values().size(); i++) {
            assertEquals(observer.values().get(0).get(i), user.getRepos().get(i));
        }
    }

    @Test
    public void putUser(User user) {

    }

    @Test
    public void putRepos(List<Repository> repos) {

    }
}