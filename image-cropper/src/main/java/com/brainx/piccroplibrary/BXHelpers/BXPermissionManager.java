package com.brainx.piccroplibrary.BXHelpers;

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

public class BXPermissionManager {

    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    public static boolean checkCameraPermission(Activity context) {
        return !(!hasSelfPermission(context, Manifest.permission.CAMERA)
                || !hasSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    public static boolean checkStoragePermission(Activity context) {
        if (hasSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) && hasSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkManageDocumentPermission(Activity context) {
        return hasSelfPermission(context, Manifest.permission.MANAGE_DOCUMENTS);
    }

    public static void requestManageDocumentPermission(Activity activity, int MANAGE_DOCUMENTS_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.MANAGE_DOCUMENTS},
                MANAGE_DOCUMENTS_REQUEST_CODE);
    }

    public static void requestStoragePermission(Activity activity, int STORAGE_PERMISSION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_REQUEST_CODE);
    }

    public static void askLocationPermission(Activity activity, int code) {
        if (!BXPermissionManager.checkLocationPermission(activity)) {
            BXPermissionManager.requestLocationPermission(activity, code);
        }
    }

    public static boolean checkLocationPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                requestCode);
    }
}


