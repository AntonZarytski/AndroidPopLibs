package com.example.anton.androidlibhomework;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class ImageConverterImpl implements ImageConverter {
    Context context;

    public ImageConverterImpl(Context context) {
        this.context = context;
    }

    @Override
    public Single<String> convertJpegToPng(String source, String dest) {
        return Single.create((SingleOnSubscribe<String>) emitter ->
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(source));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, context.getContentResolver().openOutputStream(Uri.parse(dest)));
                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.computation());
    }
}
