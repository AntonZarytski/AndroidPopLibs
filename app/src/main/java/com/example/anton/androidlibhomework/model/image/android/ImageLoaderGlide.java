package com.example.anton.androidlibhomework.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anton.androidlibhomework.NetworkStatus;
import com.example.anton.androidlibhomework.model.cache.IImageCache;
import com.example.anton.androidlibhomework.model.image.ImageLoader;

import timber.log.Timber;

public class ImageLoaderGlide implements ImageLoader<ImageView> {
    private static final String TAG = "ImageLoaderGlide";

    private IImageCache imageCache;

    public ImageLoaderGlide(IImageCache imageCache) {
        this.imageCache = imageCache;
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
                    imageCache.saveImage(url, resource);
                    return false;
                }
            }).into(container);
        } else {
            if (imageCache.contains(url)) {
                GlideApp.with(container.getContext())
                        .load(imageCache.getFile(url))
                        .into(container);
            }
        }
    }


}
