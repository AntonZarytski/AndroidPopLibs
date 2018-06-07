package com.example.anton.androidlibhomework.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.anton.androidlibhomework.RepoRowView;
import com.example.anton.androidlibhomework.model.common.NetworkStatus;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.repo.CacheWorker;
import com.example.anton.androidlibhomework.model.repo.RealmUserRepo;
import com.example.anton.androidlibhomework.views.MainView;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IRepoListPresenter {
    private Scheduler mainThreadScheduler;
    //    private PaperUsersRepo userRepo;
//    private AAUserRepo userRepo;
//    private RealmUserRepo userRepo;
    private GitHubUserModel userModel;
    private CacheWorker userRepo;

    public MainPresenter(Scheduler mainThreadScheduler) {
        this.mainThreadScheduler = mainThreadScheduler;
//        userRepo = new PaperUsersRepo();
//        userRepo = new AAUserRepo();
        userRepo = new RealmUserRepo();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @SuppressLint("CheckResult")
    public void loadUserData(final String userName) {
        getViewState().setProgresVisibility(true);
        if (!NetworkStatus.isOffline()) {
            userRepo.downloadAndPutUser(userName)
                    .observeOn(mainThreadScheduler)
                    .subscribe(user -> {
                        this.userModel = user;
                        getViewState().setProgresVisibility(false);
                        getViewState().loadImage(user.getAvatarUrl());
                        getViewState().setUsernameText(user.getLogin());
                        userRepo.downloadAndPutRepos(user)
                                .observeOn(mainThreadScheduler)
                                .subscribe(repositories -> {
                                    this.userModel.setRepos(repositories);
                                    for (int i = 0; i < repositories.size(); i++) {
                                        getViewState().setRepos(repositories.get(i).getName(), i);
                                    }
                                }, throwable -> {
                                    Timber.e(throwable, "Failed to get user repos");
                                    getViewState().setUsernameText(throwable.getMessage());
                                    throwable.printStackTrace();
                                    getViewState().setProgresVisibility(false);
                                });


                    }, throwable -> {
                        Timber.e(throwable, "Failed to get user");
                        throwable.printStackTrace();
                        getViewState().setProgresVisibility(false);
                        getViewState().setUsernameText(throwable.getMessage());
                    });
        } else {
            userRepo.getUser(userName).observeOn(mainThreadScheduler)
                    .subscribe(user -> {
                        this.userModel = user;
                        getViewState().setProgresVisibility(false);
                        getViewState().loadImage(user.getAvatarUrl());
                        getViewState().setUsernameText(user.getLogin());
                        userRepo.getUserRepos(user)
                                .observeOn(mainThreadScheduler)
                                .subscribe(repositories -> {
                                    this.userModel.setRepos(repositories);
                                    for (int i = 0; i < repositories.size(); i++) {
                                        getViewState().setRepos(repositories.get(i).getName(), i);
                                    }
                                }, throwable -> {
                                    Timber.e(throwable, "Failed to get user repos");
                                    getViewState().setUsernameText(throwable.getMessage());
                                    throwable.printStackTrace();
                                    getViewState().setProgresVisibility(false);
                                });


                    }, throwable -> {
                        Timber.e(throwable, "Failed to get user");
                        throwable.printStackTrace();
                        getViewState().setProgresVisibility(false);
                        getViewState().setUsernameText(throwable.getMessage());
                    });
        }
    }

    @Override
    public void bindRepoListRow(int pos, RepoRowView rowViev) {
        if (userModel != null) {
            rowViev.SetTittle(userModel.getRepos().get(pos).getName());
        }
    }

    @Override
    public int getRepoCount() {
        return userModel == null || userModel.getRepos() == null ? 0 : userModel.getRepos().size();
    }
}
