package com.example.anton.androidlibhomework;

import android.util.Log;

import com.example.anton.androidlibhomework.di.DaggerTestComponent;
import com.example.anton.androidlibhomework.di.TestComponent;
import com.example.anton.androidlibhomework.di.modules.TestRepoModule;
import com.example.anton.androidlibhomework.model.entity.User;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;
import com.example.anton.androidlibhomework.presenter.MainPresenter;
import com.example.anton.androidlibhomework.view.MainView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import timber.log.Timber;

public class MainPresenterUnitTest {
    @Mock
    MainView mainView;
    private MainPresenter presenter;
    private TestScheduler testScheduler;

    @BeforeClass
    public static void setupClass() {
        Timber.plant(new Timber.DebugTree());
        Log.d("TEST", "setupClass");
    }

    @AfterClass
    public static void tearDownClass() {
        Log.d("TEST", "tearDownClass");
    }

    @Before
    public void setup() {
        Log.d("TEST", "setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new MainPresenter(testScheduler));
    }

    @After
    public void tearDown() {
        Log.d("TEST", "tearDown");
    }

    @Test
    public void onFirstViewAttach() {
        presenter.attachView(mainView);
        Mockito.verify(mainView).init();
    }

    @Test
    public void loadInfoSuccess() {
        User user = new User("AntonZarytski", "avatarUrl");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public UsersRepo usersRepo() {
                        UsersRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser(user.getLogin())).thenReturn(Observable.just(user));
                        Mockito.when(repo.getUserRepos(user)).thenReturn(Observable.just(new ArrayList<>()));
                        return repo;
                    }
                }).build();
        component.inject(presenter);

        presenter.attachView(mainView);
        presenter.loadInfo();
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).showAvatar(user.getAvatarUrl());
        Mockito.verify(mainView).setUsername(user.getLogin());
        Mockito.verify(mainView).updateRepoList();
    }

    @Test
    public void loadInfoFail() {
        String error = "error";
        User user = new User("AntonZarytski", "avatarUrl");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public UsersRepo usersRepo() {
                        UsersRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser(user.getLogin())).thenReturn(Observable.just(user));
                        Mockito.when(repo.getUserRepos(user)).thenReturn(Observable.error(new RuntimeException(error)));
                        return repo;
                    }
                }).build();

        component.inject(presenter);
        presenter.attachView(mainView);
        presenter.loadInfo();
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).showError(error);
        Mockito.verify(mainView).hideLoading();
    }

}
