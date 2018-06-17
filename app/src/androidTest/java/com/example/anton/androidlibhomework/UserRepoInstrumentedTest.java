package com.example.anton.androidlibhomework;

import com.example.anton.androidlibhomework.di.DaggerTestComponent;
import com.example.anton.androidlibhomework.di.TestComponent;
import com.example.anton.androidlibhomework.di.modules.ApiModule;
import com.example.anton.androidlibhomework.model.entity.Repository;
import com.example.anton.androidlibhomework.model.entity.User;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;


public class UserRepoInstrumentedTest {
    private static MockWebServer mockWebServer;
    @Inject
    UsersRepo usersRepo;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup() {
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule() {
                    @Override
                    public String endpoint() {
                        return mockWebServer.url("/").toString();
                    }
                }).build();

        component.inject(this);
    }

    @After
    public void tearDown() {

    }


    @Test
    public void getUser() {
        mockWebServer.enqueue(createUserResponse("somelogin", "someurl"));

        TestObserver<User> observer = new TestObserver<>();
        usersRepo.getUser("somelogin").subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(), "somelogin");
        assertEquals(observer.values().get(0).getAvatarUrl(), "someurl");
    }

    private MockResponse createUserResponse(String login, String avatarUrl) {
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\"}";
        return new MockResponse()
                .setBody(body);
    }

    @Test
    public void getRepos(User user) {

        mockWebServer.enqueue(createReposResponse(user.getLogin()));
        TestObserver<List<Repository>> observer = new TestObserver<>();
        usersRepo.getUserRepos(user).subscribe(observer);

        observer.awaitTerminalEvent();

        assertEquals(observer.values().get(0), user.getRepos().get(0));
    }

    private MockResponse createReposResponse(String userLogin) {
        String body = "{\"id\":\"" + "repos_id" + "\", \"name\":\"" + "repos_name" + "\"}";
        return new MockResponse().setBody(body);

    }
}
