package com.brainx.piccroplibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.brainx.piccroplibrary.PCLActivity.PCLCropActivity;
import com.brainx.piccroplibrary.PCLHelpers.PCLConstants;


/**
 * Created by brainx on 4/5/18.
 */

public class PCLCrop {


    private static PCLCrop pclCrop;
    private Intent cropIntent;
    private Context context;
    private int CROP_REQUEST_CODE = 1478;


    public static PCLCrop getInstance() {
        if (pclCrop == null) {
            pclCrop = new PCLCrop();
        }
        return pclCrop;
    }

    public PCLCrop initlization(Context icontext) {
        cropIntent = new Intent(icontext, PCLCropActivity.class);
        context = icontext;
        return pclCrop;
    }


    public PCLCrop setUri(Uri sourceUri, Uri destinationUri) {
        if (cropIntent != null && sourceUri != null && destinationUri != null) {
            cropIntent.putExtra(PCLConstants.SOURCE_URI, sourceUri.toString());
            cropIntent.putExtra(PCLConstants.DESTINATION_URI, destinationUri.toString());
        }
        return pclCrop;
    }

    public PCLCrop withAspectRatio(double aspectRatioX, double aspectRatioY) {
        if (cropIntent != null) {
            cropIntent.putExtra(PCLConstants.ASPECT_RATION_X, aspectRatioX);
            cropIntent.putExtra(PCLConstants.ASPECT_RATION_Y, aspectRatioY);
        }
        return pclCrop;

    }

    public void crop(int requestCode) {
        CROP_REQUEST_CODE = requestCode;
        if (cropIntent != null && CROP_REQUEST_CODE >= 0) {
            ((Activity) context).startActivityForResult(cropIntent, CROP_REQUEST_CODE);
        }
    }

    public void crop() {
        if (cropIntent != null && CROP_REQUEST_CODE >= 0) {
            ((Activity) context).startActivityForResult(cropIntent, CROP_REQUEST_CODE);
        }
    }
}
