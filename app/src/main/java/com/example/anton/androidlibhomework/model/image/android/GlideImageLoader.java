package com.example.anton.androidlibhomework.model.image.android;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anton.androidlibhomework.model.cache.ImageCache;
import com.example.anton.androidlibhomework.model.common.NetworkStatus;
import com.example.anton.androidlibhomework.model.image.IimageLoader;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.annotations.Nullable;
import timber.log.Timber;

public class GlideImageLoader implements IimageLoader<ImageView> {
    public static final String IMAGE_BOOK = "images";
    private final String pathToSaveImage = Environment.getExternalStorageDirectory().toString();


    public static String MD5(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOnline()) {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Image load failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ImageCache.saveImage(url, resource);
                    return false;
                }
            }).into(container);
        } else {
            if (ImageCache.contains(url)) {
                GlideApp.with(container.getContext())
                        .load(ImageCache.getFile(url))
                        .into(container);
            }
        }
    }
}

