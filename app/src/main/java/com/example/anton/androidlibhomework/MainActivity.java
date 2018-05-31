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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import java.io.File;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * В активити все делал, вроде работает, а в mvp вообще не понимаю что куда вставлять((
 * и в данном случае интерфейс непонимаю зачем нужен тут
 */
public class MainActivity extends MvpAppCompatActivity implements MainView {
    private static final int LOADED_PHOTO = 345;
    private static final int READ_STORAGE = 467;
    private MainPresenter presenter;
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
        presenter = new MainPresenter();
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

            presenter.savePathPicture(cursor.getString(columnIndex));
            cursor.close();

            Bitmap photo = BitmapFactory.decodeFile(presenter.getPathPicture());

            choosenPhoto.clearColorFilter();
            choosenPhoto.setImageBitmap(photo);
        }
    }

    @OnClick(R.id.convert_to_png_btn)
    public void convertToPng() {
        presenter.convertPhotoBackground(getCallable()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                //TODO анимация
            }

            @Override
            public void onError(Throwable e) {
                //TODO конец анимации
            }

            @Override
            public void onComplete() {
                //TODO конец анимации
            }
        });
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
                presenter.setSavedPath(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedBitmap.png").toString());
                return temp.compress(Bitmap.CompressFormat.PNG, 100, presenter.convert(new File(presenter.getSavedPath())));
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

    @Override
    public void getFile() {
    }

    @Override
    public void writeFile() {
    }
}
