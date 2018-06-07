package com.example.anton.androidlibhomework.model.image.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anton.androidlibhomework.model.common.NetworkStatus;
import com.example.anton.androidlibhomework.model.common.Utils;
import com.example.anton.androidlibhomework.model.image.IimageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import io.paperdb.Paper;
import timber.log.Timber;

public class GlideImageLoader implements IimageLoader<ImageView> {
    public static final String IMAGE_BOOK = "images";
    private Activity activity;


    @SuppressLint("CheckResult")
    @Override
    public void loadInto(final String url, ImageView container) {
        //GlideApp позволяет делать доп операции с image маштаб, поворот, наклон и тд
        if (NetworkStatus.isOffline()) {
//            String md5 = Utils.MD5(url);
//            if (Paper.book(IMAGE_BOOK).contains(md5)) {
//                byte[] bytes = Paper.book(IMAGE_BOOK).read(md5);
//                Glide.with(container.getContext())
//                        .load(bytes)
//                        .into(container);
//        }
            Bitmap loadedImage = loadImage(url);
            container.setImageBitmap(loadedImage);

        } else {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Failed load Image");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    saveImage(resource, url);
                    return true;
                }
            }).into(container);
        }
    }

    Bitmap loadImage(String url) {
        String filePath = Paper.book(IMAGE_BOOK).read(Utils.MD5(url));
        File file = new File(filePath);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Bitmap image = (Bitmap) ois.readObject();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean saveImage(Bitmap image, String url) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), image.toString());
        FileOutputStream fos = null;
        try {
            try {
                fos = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Paper.book(IMAGE_BOOK).write(Utils.MD5(url), file.toString());
                return true;
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
