package com.example.anton.androidlibhomework.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anton.androidlibhomework.model.common.NetworkStatus;
import com.example.anton.androidlibhomework.model.common.Utils;
import com.example.anton.androidlibhomework.model.image.IimageLoader;

import java.io.ByteArrayOutputStream;

import io.paperdb.Paper;
import timber.log.Timber;

public class GlideImageLoader implements IimageLoader<ImageView> {
    public static final String IMAGE_BOOK = "images";


    @Override
    public void loadInto(final String url, ImageView container) {
        //GlideApp позволяет делать доп операции с image маштаб, поворот, наклон и тд
        if (NetworkStatus.isOffline()) {
            String md5 = Utils.MD5(url);
            if (Paper.book(IMAGE_BOOK).contains(md5)) {
                byte[] bytes = Paper.book(IMAGE_BOOK).read(md5);
                Glide.with(container.getContext())
                        .load(bytes)
                        .into(container);
            }
        } else {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Failed load Image");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Paper.book(IMAGE_BOOK).write(Utils.MD5(url), stream.toByteArray());
                    return false;
                }
            }).into(container);
        }
    }
}
