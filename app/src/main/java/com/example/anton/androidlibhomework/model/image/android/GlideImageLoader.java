package com.example.anton.androidlibhomework.model.image.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;
import timber.log.Timber;

public class GlideImageLoader implements IimageLoader<ImageView> {
    public static final String IMAGE_BOOK = "images";
    private final String pathToSaveImage = Environment.getExternalStorageDirectory().toString();

    @SuppressLint("CheckResult")
    @Override
    public void loadInto(final String url, ImageView container) {
        //GlideApp позволяет делать доп операции с image маштаб, поворот, наклон и тд
        if (NetworkStatus.isOffline()) {
            String md5 = Utils.MD5(url);
            if (Paper.book(IMAGE_BOOK).contains(md5)) {
                String filePath = Paper.book(IMAGE_BOOK).read(md5);
                Bitmap loadedImage = loadImage(filePath);
                container.setImageBitmap(loadedImage);
//                Glide.with(container.getContext())
//                        .load(loadedImage)
//                        .into(container);
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
                    String savedPath = saveImage(resource);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Paper.book(IMAGE_BOOK).write(Utils.MD5(url), savedPath);
                    return false;
                }
            }).into(container);
        }
    }

    Bitmap loadImage(String filePath) {
        InputStream fIn = null;
        try {
            File file = new File(filePath);
            fIn = new FileInputStream(file);
            Bitmap image = BitmapFactory.decodeStream(fIn);
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    String saveImage(Bitmap image) {
        OutputStream fOut = null;
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        try {
            File file = new File(pathToSaveImage, Integer.toString(time.getYear()) +
                    Integer.toString(time.getMonth()) + Integer.toString(time.getDay()) +
                    Integer.toString(time.getHours()) + Integer.toString(time.getMinutes()) +
                    Integer.toString(time.getSeconds()) + ".jpg");
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file.toString();
            // TODO регистрация в фотоальбоме. Можно ли в GlideImageLoader передать context? или как правильно сделать?
//            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (Exception e) {
            return "";
        }
    }
}

