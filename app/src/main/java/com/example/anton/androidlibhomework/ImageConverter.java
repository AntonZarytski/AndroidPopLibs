package com.example.anton.androidlibhomework;

import io.reactivex.Single;


public interface ImageConverter {
    Single<String> convertJpegToPng(String source, String dest);
}
