package com.example.anton.androidlibhomework.model.image.android;

import android.widget.ImageView;

import com.example.anton.androidlibhomework.model.image.IimageLoader;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader implements IimageLoader<ImageView> {
    @Override
    public void loadInto(String url, ImageView container) {
        Picasso.get().load(url).into(container);
    }
}
