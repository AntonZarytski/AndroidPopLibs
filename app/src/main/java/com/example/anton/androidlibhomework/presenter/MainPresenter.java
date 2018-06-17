package com.example.anton.androidlibhomework.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.anton.androidlibhomework.model.entity.User;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;
import com.example.anton.androidlibhomework.view.MainView;
import com.example.anton.androidlibhomework.view.RepoRowView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IRepoListPresenter {
    @Inject
    UsersRepo usersRepo;
    private Scheduler scheduler;
    private User user;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    @SuppressLint("CheckResult")
    public void loadInfo() {
        usersRepo.getUser("AntonZarytski")
                .observeOn(scheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    usersRepo.getUserRepos(user)
                            .observeOn(scheduler)
                            .subscribe(userRepositories -> {
                                this.user.setRepos(userRepositories);
                                getViewState().hideLoading();
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                                getViewState().hideLoading();
                            });
                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                    getViewState().showError(throwable.getMessage());
                    getViewState().hideLoading();
                });

    }

    public void onPermissionsGranted() {
        loadInfo();
    }

    @Override
    public void bindRepoListRow(int pos, RepoRowView rowView) {
        if (user != null) {
            rowView.setTitle(user.getRepos().get(pos).getName());
        }
    }

    @Override
    public int getRepoCount() {
        return user == null || user.getRepos() == null ? 0 : user.getRepos().size();
    }
}
