package com.brainx.piccroplibrary.PCLHelpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.brainx.piccroplibrary.BuildConfig;
import com.brainx.piccroplibrary.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by tahir zahoor on 06/07/2017.
 */

public class PCLUtils {

    public static String convertStringToPascal(String str) {

        String newString = "";
        str = str.toLowerCase();
        if (!str.isEmpty()) {
            String[] arr = str.split(" ");
            for (String val : arr) {
                newString += String.valueOf(val.charAt(0)).toUpperCase() + val.substring(1, val.length()) + " ";
            }

            return newString.trim();
        } else
            return str;
    }


    public static Uri getUriFromFile(Context context, File file) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        }
    }

    public static void deleteImageFromExternalStorage(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

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

    public static Bitmap getBitmapFromPath(String filePath) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd, filePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(c.getTime());
    }

    public static String getStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        return encodeImage(ba);
    }

    private static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


    public static Bitmap getSquareScaledImage(Bitmap bitmap, int desiredSide) throws OutOfMemoryError {

        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();


        float desiredScale;
        if (srcWidth > srcHeight)
            desiredScale = (float) desiredSide / srcWidth;
        else
            desiredScale = (float) desiredSide / srcHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(desiredScale, desiredScale);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static boolean correctRotationForCameraPic(String path) {
        int rotate = getImageOrientation(path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        try {
            FileOutputStream fos = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void applyFontForToolbarTitle(Activity context, String font) {
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        if (toolbar != null)
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View view = toolbar.getChildAt(i);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    Typeface titleFont = Typeface.
                            createFromAsset(context.getAssets(), font);
                    if (tv.getText().equals(toolbar.getTitle())) {
                        tv.setTypeface(titleFont);
                        break;
                    }
                }
            }
    }


    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getViewRelativeTop(View myView, View parent) {
        if (myView.getParent() == parent)
            return myView.getTop();
        else
            return myView.getTop() + getViewRelativeTop((View) myView.getParent(), parent);
    }


    public static String imageNameCreate() {
        Random rand = new Random();

        String date = new SimpleDateFormat("yyyyMMddHHmmss'.jpg'").format(new Date());
        return date;
    }

    public static String getCompleteAddressString(Context c, double LATITUDE, double LONGITUDE) {
        String strAdd = "(GPS location unavailable)";
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() != 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
        }
        return strAdd;
    }


}
