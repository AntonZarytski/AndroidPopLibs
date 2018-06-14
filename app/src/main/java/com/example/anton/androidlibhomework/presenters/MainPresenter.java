package com.example.anton.androidlibhomework.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.anton.androidlibhomework.RepoRowView;
import com.example.anton.androidlibhomework.model.entity.GitHubUserModel;
import com.example.anton.androidlibhomework.model.repo.UsersRepo;
import com.example.anton.androidlibhomework.views.MainView;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IRepoListPresenter {
    private Scheduler mainThreadScheduler;
    //    private CacheWorker userRepoRealm;
    @Inject
    @Named("ForRealm")
    public UsersRepo userRepoRealm;
    @Inject
    @Named("ForPsper")
    public UsersRepo userRepoPaper;
    //    private PaperUsersRepo userRepoRealm;
//    private AAUserRepo userRepoRealm;
//    private RealmUserRepo userRepoRealm;
    private GitHubUserModel userModel;

    public MainPresenter(Scheduler mainThreadScheduler) {
        this.mainThreadScheduler = mainThreadScheduler;
//        userRepoRealm = new PaperUsersRepo();
//        userRepoRealm = new AAUserRepo();
//        userRepoRealm = new RealmUserRepo();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @SuppressLint("CheckResult")
    public void loadUserData(final String userName) {
        userRepoRealm.getUser(userName).subscribe(user -> {
            this.userModel = user;
            userRepoRealm.getUserRepos(user)
                    .observeOn(mainThreadScheduler)
                    .subscribe(userRepositories -> {
                        this.userModel.setRepos(userRepositories);
                        getViewState().hideLoading();
                        getViewState().setUsername(user.getLogin());
                        getViewState().showAvatar(user.getAvatarUrl());
                        getViewState().updateRepoList();
                    }, throwable -> {
                        Timber.e(throwable, "Failed to get user repos");
                        getViewState().showError(throwable.getMessage());
                        getViewState().hideLoading();
                    });
        }, throwable -> {
            Timber.e(throwable, "Failed to get user");
            mainThreadScheduler.scheduleDirect(() -> {
                getViewState().showError(throwable.getMessage());
                getViewState().hideLoading();
            });
        });
    }

    public void onPermissionsGranted() {

    }

    @Override
    public void bindRepoListRow(int pos, RepoRowView rowView) {
        if (userModel != null) {
            rowView.SetTittle(userModel.getRepos().get(pos).getName());
        }
    }

    @Override
    public int getRepoCount() {
        return userModel == null || userModel.getRepos() == null ? 0 : userModel.getRepos().size();
    }
}
