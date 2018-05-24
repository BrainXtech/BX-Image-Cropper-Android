package com.brainx.piccroplibrarytest.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by tahir zahoor on 07/07/2017.
 */

public class PermissionManager {

    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    public static boolean checkStoragePermission(Activity context) {
        return hasSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    public static void requestStoragePermission(Activity activity, int STORAGE_PERMISSION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_REQUEST_CODE);
    }
}


