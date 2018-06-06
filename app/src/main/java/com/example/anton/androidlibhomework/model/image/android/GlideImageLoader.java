package com.example.anton.androidlibhomework.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anton.androidlibhomework.model.image.IimageLoader;

public class GlideImageLoader implements IimageLoader<ImageView> {

    @Override
    public void loadInto(String url, ImageView container) {
        //GlideApp позволяет делать доп операции с image маштаб, поворот, наклон и тд
//        GlideApp.with(container.getContext()).load(url).into(container);
        //Glide.with(container.getContext()).load(url).into(container);
        GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                return true;
            }
        }).into(container);
    }
}
