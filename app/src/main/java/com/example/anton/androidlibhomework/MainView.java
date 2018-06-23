package com.example.anton.androidlibhomework;


public interface MainView {
    void pickImage();

    void showConvertProgressDialog();

    void dismissConvertProgressDialog();

    void showConvertationSuccessMessage();

    void showConvertationFailedMessage();
}
