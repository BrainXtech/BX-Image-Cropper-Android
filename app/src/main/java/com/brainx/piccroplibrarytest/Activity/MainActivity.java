package com.brainx.piccroplibrarytest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.brainx.piccroplibrary.PCLCrop;
import com.brainx.piccroplibrarytest.Helpers.PermissionManager;
import com.brainx.piccroplibrarytest.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE = 100;
    private static final int GALLERY_INTENT = 1222;
    private static final int CROP_REQUEST_CODE = 1478;
    private String imageTypeSet;

    Button cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setListener() {
        cropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storagePermission())
                    openGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = Uri.parse(data.getData().toString());
                    cropCameraCaptureImage(uri);
                }
            }
        }
    }


    private void init() {
        cropImageView = findViewById(R.id.crop_image);
    }

    private boolean storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionManager.checkStoragePermission(this)) {
                return true;
            } else {
                PermissionManager.requestStoragePermission(this, REQUEST_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);
    }

    private void cropCameraCaptureImage(Uri uri) {
        PCLCrop.getInstance().initlization(this).setUri(uri, uri).withAspectRatio(1, 2).crop(CROP_REQUEST_CODE);

    }


}
