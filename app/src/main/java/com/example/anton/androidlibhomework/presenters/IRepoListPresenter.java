package com.example.anton.androidlibhomework.presenters;

import com.example.anton.androidlibhomework.RepoRowView;


public interface IRepoListPresenter {
    void bindRepoListRow(int pos, RepoRowView rowView);

    int getRepoCount();
}

