package com.example.anton.androidpoplibs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends MvpAppCompatActivity implements ActivityMain {

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.edit_text)
    EditText editText;

    private Observer<String> observer;
    private RxEventBus eventBus;
    @InjectPresenter
    Presenter presenter;

    @ProvidePresenter
    public Presenter createPresenter() {
        return new Presenter();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new Presenter();

        Disposable disposable = ((App) getApplication()).getEventBus().getObservable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof SomeEvent1) {
                    Toast.makeText(MainActivity.this, "SomeEvent2", Toast.LENGTH_SHORT).show();

                }
                if (o instanceof SomeEvent2) {
                    Toast.makeText(MainActivity.this, "SomeEvent2", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((App) getApplication()).getEventBus().send(new SomeEvent1());


        //TODO альтернатива addTextChangedListener(new TextWatcher())
//        Disposable disposable1 = RxTextView.textChanges(editText).subscribe(new Consumer<CharSequence>()
//        {
//            @Override
//            public void accept(CharSequence charSequence) throws Exception
//            {
//                textView.setText(charSequence);
//            }
//        });


        observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.CharIsEntered(s.toString());
                presenter.getObservable().subscribe(observer);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setText(String text) {
        textView.setText(text);
    }
}
