package com.example.anton.androidlibhomework.model.image.android;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.anton.androidlibhomework.model.image.ImageLoader;
import com.squareup.picasso.Picasso;

public class ImageLoaderPicasso implements ImageLoader<ImageView> {
    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        Picasso.get().load(url).into(container);
    }
}
