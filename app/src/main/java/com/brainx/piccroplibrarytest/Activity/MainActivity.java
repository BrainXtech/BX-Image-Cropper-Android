package com.brainx.piccroplibrarytest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.brainx.bxcroplibrary.BXCrop;
import com.brainx.bxcroplibrary.BXHelpers.BXCropResult;
import com.brainx.piccroplibrarytest.Helpers.PermissionManager;
import com.brainx.piccroplibrarytest.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //region Properties
    private static final int REQUEST_STORAGE = 100;
    private static final int GALLERY_INTENT = 1222;
    private static final int CROP_REQUEST_CODE = 1478;

    private Button selectImageView;
    private ImageView cropImageView;

    //endregion

    //region Lifecycle
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = Uri.parse(data.getData().toString());
                    cropImage(uri);
                }
            }
        } else if (requestCode == CROP_REQUEST_CODE) {
            if (resultCode == BXCropResult.RESULT_OK) {
                if (data != null) {
                    Picasso.with(MainActivity.this).load(new File(BXCropResult.getPath(data))).into(cropImageView);
                }
            }
        }
    }

    //endregion

    //region Listener

    private void setListener() {
        selectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storagePermission())
                    openGallery();
            }
        });
    }

    //endregion

    //region initialization

    private void init() {
        selectImageView = findViewById(R.id.select_button);
        cropImageView = findViewById(R.id.crop_image_view);
    }

    //endregion

    //region Private Method
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

    private void cropImage(Uri uri) {
        Uri destinationUri = destinationUri();
        if (destinationUri != null) {
            BXCrop.getInstance().initialization(this).setUri(uri, destinationUri()).withAspectRatio(1, 2).crop(CROP_REQUEST_CODE);
        }
    }

    private Uri destinationUri() {
        if (PermissionManager.checkStoragePermission(this)) {
            String root = Environment.getExternalStorageDirectory().toString() + "/BXCrop";
            String iName = new SimpleDateFormat("yyyyMMddHHmmss'.jpg'").format(new Date());
            return Uri.fromFile(new File(root, iName));

        } else {
            return null;
        }
    }

    //endregion

}
