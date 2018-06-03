package com.example.anton.androidlibhomework;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private Scheduler mainThreadScheduler;
    private UsersRepo usersRepo;

    public MainPresenter(Scheduler mainThreadScheduler) {
        this.mainThreadScheduler = mainThreadScheduler;
        usersRepo = new UsersRepo();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @SuppressLint("CheckResult")
    public void loadUserData(final String userName) {
        MainPresenter.this.getViewState().setProgresVisibility(true);
        usersRepo.getUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThreadScheduler)
                .subscribe(new Consumer<GitHubUserModel>() {
                    @Override
                    public void accept(GitHubUserModel gitHubUserModel) throws Exception {
                        MainPresenter.this.getViewState().setUsernameText(gitHubUserModel.getLogin());
                        MainPresenter.this.getViewState().setId(String.valueOf(gitHubUserModel.getId()));
                        MainPresenter.this.getViewState().loadImage(gitHubUserModel.getAvatarUrl());
                        loadReposData(userName);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MainPresenter.this.getViewState().setUsernameText(throwable.getMessage());
                        MainPresenter.this.getViewState().setProgresVisibility(false);

                    }
                });

    }

    @SuppressLint("CheckResult")
    public void loadReposData(String username) {
        usersRepo.getRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThreadScheduler)
                .subscribe(new Consumer<List<GitHubUsersRepos>>() {
                    @Override
                    public void accept(List<GitHubUsersRepos> gitHubUsersRepos) throws Exception {
                        for (int i = 0; i < gitHubUsersRepos.size(); i++) {
                            MainPresenter.this.getViewState().setRepos(gitHubUsersRepos.get(i).getName(), i + 1);
                        }
                        MainPresenter.this.getViewState().setProgresVisibility(false);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MainPresenter.this.getViewState().setUsernameText(throwable.getMessage());
                        MainPresenter.this.getViewState().setProgresVisibility(false);
                    }
                });
    }
}
