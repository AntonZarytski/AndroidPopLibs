package com.example.anton.androidlibhomework;

import android.os.Bundle;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @InjectPresenter
    MainPresenter presenter;
    @BindView(R.id.btn_one)
    Button button1;
    @BindView(R.id.btn_two)
    Button button2;
    @BindView(R.id.btn_three)
    Button button3;
    private Observer<Integer> observer1;
    private Observer<Integer> observer2;
    private Observer<Integer> observer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        observer1 = initObserver(observer1, button1);
        observer2 = initObserver(observer2, button2);
        observer3 = initObserver(observer3, button3);

    }

    private Observer<Integer> initObserver(Observer<Integer> observer, final Button button) {
        observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                button.setText(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        return observer;
    }

    @ProvidePresenter
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @OnClick({R.id.btn_one})
    public void buttonOneClick(Button button) {
        presenter.onButtonOneClick();
    }

    @OnClick({R.id.btn_two})
    public void buttonTwoClick(Button button) {
        presenter.onButtonTwoClick();
    }

    @OnClick({R.id.btn_three})
    public void buttonThreeClick(Button button) {
        presenter.onButtonThreeClick();
    }


    @Override
    public void setOneValue(Observable<Integer> observable) {
        observable.subscribe(observer1);
    }

    @Override
    public void setTwoValue(Observable<Integer> observable) {
        observable.subscribe(observer2);
    }

    @Override
    public void setThreeValue(Observable<Integer> observable) {
        observable.subscribe(observer3);
    }
}
