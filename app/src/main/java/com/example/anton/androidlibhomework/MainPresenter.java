package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private CounterModel model;
    private Observer<Integer> observer1;
    private Observer<Integer> observer2;
    private Observer<Integer> observer3;

    public MainPresenter() {
        this.model = new CounterModel();
        observer1 = initObserver(1);
        observer2 = initObserver(2);
        observer3 = initObserver(3);
    }

    private Observer<Integer> initObserver(final int position) {
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
                if (position == 1) {
                    getViewState().setOneValue(integer);
                } else if (position == 2) {
                    getViewState().setTwoValue(integer);
                } else if (position == 3) {
                    getViewState().setThreeValue(integer);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public Observable<Integer> calcValue(int index) {
        model.setAt(index, model.getAt(index) + 1);
        return model.getObservable(index);
    }

    public void onButtonOneClick() {
        calcValue(0).subscribe(observer1);
    }

    public void onButtonTwoClick() {
        calcValue(1).subscribe(observer2);
    }

    public void onButtonThreeClick() {
        calcValue(2).subscribe(observer3);
    }

}
