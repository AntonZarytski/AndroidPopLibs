package com.example.anton.androidlibhomework.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anton.androidlibhomework.R;
import com.example.anton.androidlibhomework.presenters.IRepoListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoRVAdapter extends RecyclerView.Adapter<RepoRVAdapter.ViewHolder> {
    IRepoListPresenter presenter;

    public RepoRVAdapter(IRepoListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.bindRepoListRow(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getRepoCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RepoRowView {

        @BindView(R.id.tv_title)
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void SetTittle(String tittle) {
            titleTextView.setText(tittle);
        }
    }
}
