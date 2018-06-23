package com.example.anton.androidlibhomework;

import android.util.Log;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class MainPresenter {
    MainView view;
    Scheduler scheduler;
    Disposable convertationSubscription;
    private ImageConverter converter;

    public MainPresenter(MainView view, Scheduler scheduler, ImageConverter converter) {
        this.view = view;
        this.scheduler = scheduler;
        this.converter = converter;
    }

    public void convertButtonClick() {
        view.pickImage();
    }

    public void convertImage(String source, String dest) {
        view.showConvertProgressDialog();
        converter.convertJpegToPng(source, dest)
                .observeOn(scheduler)
                .subscribeWith(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        convertationSubscription = d;
                    }

                    @Override
                    public void onSuccess(String s) {
                        view.showConvertationSuccessMessage();
                        view.dismissConvertProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "error", e);
                        view.showConvertationFailedMessage();
                        view.dismissConvertProgressDialog();
                    }
                });
    }

    public void onConvertationCanceled() {
        if (convertationSubscription != null && !convertationSubscription.isDisposed()) {
            convertationSubscription.dispose();
            view.dismissConvertProgressDialog();
        }
    }
}
