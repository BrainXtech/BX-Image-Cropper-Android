package com.brainx.piccroplibrary.PCLActivity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.brainx.piccroplibrary.BaseClasses.BaseActivity;
import com.brainx.piccroplibrary.PCLAsyncTask.PCLSaveImageTask;
import com.brainx.piccroplibrary.PCLHelpers.PCLConstants;
import com.brainx.piccroplibrary.PCLHelpers.PCLFilePath;
import com.brainx.piccroplibrary.PCLHelpers.PCLPermissionManager;
import com.brainx.piccroplibrary.PCLHelpers.PCLRequestConstant;
import com.brainx.piccroplibrary.PCLHelpers.PCLUtils;
import com.brainx.piccroplibrary.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class PCLCropActivity extends BaseActivity implements CropImageView.OnCropImageCompleteListener, PCLSaveImageTask.ISaveImageTask {

    //region Properties
    private static final int MAX_SIDE = 800;
    private static final int GALLERY_MAX_SIDE = 1000;
    private CropImageView cropImageView;
    private Uri sourceUri;
    private Uri destinationUri;
    private int aspectRatioX;
    private int aspectRatioY;
    String selectedFilePath;
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
        if (requestCode == PCLRequestConstant.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (sourceUri != null)
                    cropImageView.setImageBitmap(getScaledImage(sourceUri));
            } else {
                Log.d("crop", "onRequestPermissionsResult: " + grantResults[0]);
            }
        }
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
            if (PCLPermissionManager.checkManageDocumentPermission(this)) {
                return true;
            } else {
                PCLPermissionManager.requestManageDocumentPermission(this, PCLRequestConstant.MANAGE_PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            return true;
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        sourceUri = Uri.parse(intent.getStringExtra(PCLConstants.SOURCE_URI));
        destinationUri = Uri.parse(intent.getStringExtra(PCLConstants.DESTINATION_URI));
        aspectRatioX = intent.getIntExtra(PCLConstants.ASPECT_RATION_X, 1);
        aspectRatioY = intent.getIntExtra(PCLConstants.ASPECT_RATION_Y, 1);
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
            bitmap = PCLUtils.getScaledImage(getBitmap(this.getContentResolver(), uri), GALLERY_MAX_SIDE);
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getRealPathFromURI( Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    //endregion

    //region CallBack
    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        Bitmap bitmap = PCLUtils.getScaledImage(result.getBitmap(), MAX_SIDE);

         selectedFilePath = PCLFilePath.getPath(this, destinationUri);
        new PCLSaveImageTask(bitmap, selectedFilePath, this).execute();
    }

    @Override
    public void onSuccessfulSave() {
        Intent data = new Intent();
        data.putExtra(PCLConstants.DESTINATION_URI,selectedFilePath);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSaveFailure() {

        finish();
    }

    //endregion

}


