package com.example.anton.androidlibhomework.presenter;


import com.example.anton.androidlibhomework.view.RepoRowView;

public interface IRepoListPresenter {
    void bindRepoListRow(int pos, RepoRowView rowView);

    int getRepoCount();
}
