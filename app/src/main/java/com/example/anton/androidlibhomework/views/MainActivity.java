package com.example.anton.androidlibhomework.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.anton.androidlibhomework.R;
import com.example.anton.androidlibhomework.model.api.ApiService;
import com.example.anton.androidlibhomework.model.image.IimageLoader;
import com.example.anton.androidlibhomework.model.image.android.GlideImageLoader;
import com.example.anton.androidlibhomework.presenters.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.btn_load)
    Button loatBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_load)
    TextView textViewLoad;
    @BindView(R.id.image_loaded_iv)
    ImageView loadedImageView;

    ApiService requestUserInfo;

    IimageLoader<ImageView> iimageLoader;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);
//        iimageLoader = new PicassoImageLoader();
        iimageLoader = new GlideImageLoader();
    }

    @OnClick({R.id.btn_load})
    public void loadUsersList() {
        presenter.loadUserData(editText.getText().toString());
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
        iimageLoader.loadInto(url, loadedImageView);
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
