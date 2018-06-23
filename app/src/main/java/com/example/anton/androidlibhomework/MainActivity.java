package com.example.anton.androidlibhomework;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String[] permissons = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSIONS_REQUEST_ID = 0;
    private static final int PICK_IMAGE_REQUEST_ID = 1;

    @BindView(R.id.convert_to_png_btn)
    Button convertButton;

    MainPresenter presenter;

    Dialog convertProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this, AndroidSchedulers.mainThread(), new ImageConverterImpl(this));
    }

    @OnClick(R.id.convert_to_png_btn)
    public void convertButtonClick() {
        presenter.convertButtonClick();
    }

    @Override
    public void pickImage() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        onPermissionsGranted();
    }

    @Override
    public void showConvertProgressDialog() {
        if (convertProgressDialog == null) {
            convertProgressDialog = new AlertDialog.Builder(this)
                    .setNegativeButton(R.string.cancel, (dialog, which) -> presenter.onConvertationCanceled())
                    .setMessage(R.string.convertation_in_progress)
                    .create();
        }

        convertProgressDialog.show();
    }

    @Override
    public void dismissConvertProgressDialog() {
        if (convertProgressDialog != null && convertProgressDialog.isShowing()) {
            convertProgressDialog.dismiss();
        }
    }

    @Override
    public void showConvertationSuccessMessage() {
        Toast.makeText(this, R.string.convertation_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConvertationFailedMessage() {
        Toast.makeText(this, R.string.convertation_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsGranted();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permissons_required)
                            .setMessage(R.string.permissions_required_message)
                            .setPositiveButton("OK", (dialog, which) -> requestPermissions())
                            .setOnCancelListener(dialog -> requestPermissions())
                            .create()
                            .show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    String outPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "result.png";
                    presenter.convertImage(imageUri.toString(), Uri.fromFile(new File(outPath)).toString());
                } catch (Exception e) {
                    //TODO : обработать ошибку как хочется
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean checkPermissions() {
        for (String permission : permissons) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissons, PERMISSIONS_REQUEST_ID);
    }

    private void onPermissionsGranted() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_ID);
    }
}
