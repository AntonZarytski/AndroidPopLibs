package com.example.anton.androidlibhomework;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.btn_load)
    Button loatBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_load)
    TextView textViewLoad;
    ApiServiceGetUser requestUserInfo;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_load})
    public void loadUsersList() {
        presenter.loadUserData(editText.getText().toString());
    }

    public boolean checkConnection(@NonNull Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void downloadOneUrl(Call<GitHubUserModel> call) throws IOException {
        call.enqueue(new Callback<GitHubUserModel>() {
            @Override
            public void onResponse(Call<GitHubUserModel> call, Response<GitHubUserModel> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        GitHubUserModel user = response.body();
                        textViewLoad.append("\n Login = " + user.getLogin()
                                + "\n Id  = " + user.getId()
                                + "\n URIAvatar = " + user.getAvatarUrl());
                    }
                } else {
                    textViewLoad.setText("OnResponse error" + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GitHubUserModel> call, Throwable t) {
                textViewLoad.setText("OnFailure error" + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        return new MainPresenter(AndroidSchedulers.mainThread());
    }

    @Override
    public void setUsernameText(String username) {
        textViewLoad.append("UserName: " + username + "\n");
    }

    @Override
    public void loadImage(String url) {
        textViewLoad.append("ImageURI: " + url + "\n");
    }

    @Override
    public void setId(String id) {
        textViewLoad.append("ImageURI: " + id + "\n");
    }

    @Override
    public void setRepos(String repoName, int i) {
        textViewLoad.append("Repository " + i + ": " + repoName + "\n");
    }

    @Override
    public void setProgresVisibility(Boolean bolean) {
        if (bolean) {
            progressBar.setVisibility(View.VISIBLE);
        } else progressBar.setVisibility(View.GONE);
    }
}
