package com.example.anton.androidlibhomework;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    FileModel model;

    public MainPresenter() {
        this.model = new FileModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }
    public void savePathPicture(String path){
        model.setPhotoPath(path);
    }
    public String getPathPicture(){
        return model.getPhotoPath();
    }
    public void setSavedPath (String path){
        model.setPathSave(path);
    }
    public String getSavedPath(){
        return model.getPathSave();
    }

    public FileOutputStream convert(File savedFile){
        FileOutputStream fos = null;
        try {
            try {
                fos = new FileOutputStream(savedFile);
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fos;
    }

    public Observable convertPhotoBackground(Callable callable) {
        return Observable.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
