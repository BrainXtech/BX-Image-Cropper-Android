package com.brainx.piccroplibrary.BXActivity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.brainx.piccroplibrary.BXBaseClasses.BaseActivity;
import com.brainx.piccroplibrary.BXAsyncTask.BXSaveImageTask;
import com.brainx.piccroplibrary.BXHelpers.BXConstants;
import com.brainx.piccroplibrary.BXHelpers.BXCropResult;
import com.brainx.piccroplibrary.BXHelpers.BXFilePath;
import com.brainx.piccroplibrary.BXHelpers.BXPermissionManager;
import com.brainx.piccroplibrary.BXHelpers.BXRequestConstant;
import com.brainx.piccroplibrary.BXHelpers.BXUtils;
import com.brainx.piccroplibrary.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class BXCropActivity extends BaseActivity implements CropImageView.OnCropImageCompleteListener, BXSaveImageTask.ISaveImageTask {

    //region Properties
    private static final int MAX_SIDE = 800;
    private static final int GALLERY_MAX_SIDE = 1000;
    private CropImageView cropImageView;
    private Uri sourceUri;
    private Uri destinationUri;
    private int aspectRatioX;
    private int aspectRatioY;
    private String destinationPath;
    //endregion

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_crop, R.id.toolbar, "Crop", true);
        super.onCreate(savedInstanceState);
        getIntentData();
        initViews();
        setListener();
        setAspectRatio();
        setImage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_crop)
            cropImage();
        else if (id == R.id.action_rotate_image)
            rotateImageLeft();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BXRequestConstant.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (sourceUri != null)
                    cropImageView.setImageBitmap(getScaledImage(sourceUri));
            } else {
                Log.d("crop", "onRequestPermissionsResult: " + grantResults[0]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(BXCropResult.RESULT_CANCELED);
    }

    //endregion

    //region Initializer
    private void initViews() {
        cropImageView = findViewById(R.id.cropImageView);
    }
    //endregion

    //region Listener
    private void setListener() {
        cropImageView.setOnCropImageCompleteListener(this);
    }
    //endregion

    //region Private method
    private boolean storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (BXPermissionManager.checkStoragePermission(this)) {
                return true;
            } else {
                BXPermissionManager.requestStoragePermission(this, BXRequestConstant.STORAGE_PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            return true;
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        sourceUri = Uri.parse(intent.getStringExtra(BXConstants.SOURCE_URI));
        destinationUri = Uri.parse(intent.getStringExtra(BXConstants.DESTINATION_URI));
        aspectRatioX = intent.getIntExtra(BXConstants.ASPECT_RATION_X, 1);
        aspectRatioY = intent.getIntExtra(BXConstants.ASPECT_RATION_Y, 1);
    }

    private void setAspectRatio() {
        cropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
    }

    private void setImage() {
        if (storagePermission() && sourceUri != null)
            cropImageView.setImageBitmap(getScaledImage(sourceUri));
    }

    private Bitmap getScaledImage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BXUtils.getScaledImage(getBitmap(this.getContentResolver(), uri), GALLERY_MAX_SIDE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void cropImage() {
        if (storagePermission()) {
            cropImageView.getCroppedImageAsync();
        }
    }

    private void rotateImageLeft() {
        cropImageView.rotateImage(90);
    }


    private void scan(String path) {
        MediaScannerConnection.scanFile(this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);

                    }
                });
    }
    //endregion

    //region CallBack
    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        Bitmap bitmap = BXUtils.getScaledImage(result.getBitmap(), MAX_SIDE);

        destinationPath = BXFilePath.getPath(this, destinationUri);
        new BXSaveImageTask(bitmap, destinationPath, this).execute();
    }

    @Override
    public void onSuccessfulSave(String filePath) {
        Intent data = new Intent();
        data.putExtra(BXConstants.DESTINATION_URI, filePath);
        setResult(BXCropResult.RESULT_OK, data);
        scan(destinationPath);
        finish();
    }

    @Override
    public void onSaveFailure() {
        setResult(BXCropResult.RESULT_CANCELED);
        finish();
    }


    //endregion

}


