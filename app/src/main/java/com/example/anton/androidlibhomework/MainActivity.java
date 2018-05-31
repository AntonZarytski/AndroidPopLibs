package com.example.anton.androidlibhomework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final int LOADED_PHOTO = 345;
    private static final int READ_STORAGE = 467;
    @BindView(R.id.choose_photo_btn)
    Button choosePhotoBtn;
    @BindView(R.id.choosen_photo_iv)
    ImageView choosenPhoto;
    @BindView(R.id.waiting)
    ImageView waiting;
    @BindView(R.id.convert_to_png_btn)
    Button convertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        waiting.animate()
                .setDuration(0)
                .alpha(0);
    }

    @OnClick(R.id.choose_photo_btn)
    public void choosePhoto() {
        requestReadStoragePermission();
    }

    private void startPickActiivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, LOADED_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOADED_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return;
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex < 0)
                return;

            String path = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = BitmapFactory.decodeFile(path);

            choosenPhoto.clearColorFilter();
            choosenPhoto.setImageBitmap(photo);
        }
    }

    @OnClick(R.id.convert_to_png_btn)
    public void convertToPng() {
        convertPhotoBackground().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (!aBoolean) {
                    throw new RuntimeException("error while convert");
                }
                waiting.animate().alpha(1);
                waiting.animate().rotation(90);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                waiting.animate().alpha(0);
            }
        });
    }

    private Observable convertPhotoBackground() {
        return Observable.fromCallable(getCallable())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @NonNull
    private Callable getCallable() {
        return new Callable() {
            @Override
            public Boolean call() throws Exception {
                Bitmap temp = ((BitmapDrawable) choosenPhoto.getDrawable()).getBitmap();
                if (temp == null) {
                    return false;
                }
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedBitmap.png");
                FileOutputStream fos = null;
                try {
                    try {
                        fos = new FileOutputStream(file);
                        temp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        return true;
                    } finally {
                        if (fos != null) fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startPickActiivity();
        } else {
            Toast.makeText(this, "Добавьте разрешение", Toast.LENGTH_LONG).show();
        }
    }
}
