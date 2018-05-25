package com.brainx.bxcroplibrary.BXHelpers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

/**
 * Created by brainx on 4/6/18.
 */

public class BXCropResult {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_FIRST_USER = 1;
    public static final int RESULT_OK = -1;

    public static String getPath(Intent data) {
        return data.getStringExtra(BXConstants.DESTINATION_URI);
    }

    public static Uri getUri(Intent data) {
        String path = getPath(data);
        if (!path.isEmpty())
            return Uri.parse(path);
        else return null;
    }

    public static Bitmap getBitmap(Intent intent) {
        String path = getPath(intent);
        if (!path.isEmpty()) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }
        return null;
    }

}
