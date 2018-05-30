package com.example.anton.androidlibhomework;

import android.os.Bundle;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @InjectPresenter
    MainPresenter presenter;
    @BindView(R.id.btn_first)
    Button button1;
    @BindView(R.id.btn_second)
    Button button2;
    @BindView(R.id.btn_third)
    Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @ProvidePresenter
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @OnClick({R.id.btn_first})
    public void buttonOneClick() {
        presenter.onButtonOneClick();
    }

    @OnClick({R.id.btn_second})
    public void buttonTwoClick() {
        presenter.onButtonTwoClick();
    }

    @OnClick({R.id.btn_third})
    public void buttonThreeClick() {
        presenter.onButtonThreeClick();
    }


    @Override
    public void setOneValue(int value) {
        button1.setText(value + "");
    }

    @Override
    public void setTwoValue(int value) {
        button2.setText(value + "");
    }

    @Override
    public void setThreeValue(int value) {
        button3.setText(value + "");
    }
}
