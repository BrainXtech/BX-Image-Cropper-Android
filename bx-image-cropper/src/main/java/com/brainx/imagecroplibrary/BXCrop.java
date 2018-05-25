package com.brainx.imagecroplibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.brainx.imagecroplibrary.BXActivity.BXCropActivity;
import com.brainx.imagecroplibrary.BXHelpers.BXConstants;


/**
 * Created by brainx on 4/5/18.
 */

public class BXCrop {

    private static BXCrop BXCrop;
    private Intent cropIntent;
    private Context context;
    public int CROP_REQUEST_CODE = 1478;


    public static BXCrop getInstance() {
        if (BXCrop == null) {
            BXCrop = new BXCrop();
        }
        return BXCrop;
    }

    public BXCrop initialization(Context icontext) {
        cropIntent = new Intent(icontext, BXCropActivity.class);
        context = icontext;
        return BXCrop;
    }


    public BXCrop setUri(Uri sourceUri, Uri destinationUri) {
        if (cropIntent != null && sourceUri != null && destinationUri != null) {
            cropIntent.putExtra(BXConstants.SOURCE_URI, sourceUri.toString());
            cropIntent.putExtra(BXConstants.DESTINATION_URI, destinationUri.toString());
        }
        return BXCrop;
    }

    public BXCrop withAspectRatio(int aspectRatioX, int aspectRatioY) {
        if (cropIntent != null) {
            cropIntent.putExtra(BXConstants.ASPECT_RATION_X, aspectRatioX);
            cropIntent.putExtra(BXConstants.ASPECT_RATION_Y, aspectRatioY);
        }
        return BXCrop;

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
