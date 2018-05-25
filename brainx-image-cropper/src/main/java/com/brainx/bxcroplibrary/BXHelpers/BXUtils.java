package com.brainx.bxcroplibrary.BXHelpers;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by tahir zahoor on 06/07/2017.
 */

public class BXUtils {


    public static Bitmap getScaledImage(Bitmap bitmap, int desiredSide) throws OutOfMemoryError {

        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();


        float desiredScale;
        if (srcWidth > srcHeight)
            desiredScale = (float) desiredSide / srcWidth;
        else
            desiredScale = (float) desiredSide / srcHeight;


        if (desiredScale < 1f) {

            Matrix matrix = new Matrix();
            matrix.postScale(desiredScale, desiredScale);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }






}
