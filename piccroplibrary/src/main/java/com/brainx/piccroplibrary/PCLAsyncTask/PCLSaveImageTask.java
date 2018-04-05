package com.brainx.piccroplibrary.PCLAsyncTask;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.OpenableColumns;

import com.brainx.piccroplibrary.PCLHelpers.PCLUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tahir zahoor on 07/07/2017.
 */

public class PCLSaveImageTask extends AsyncTask<Void, Void, Boolean> {


    private String filePath;
    private Bitmap image;
    private ISaveImageTask mListener;

    public PCLSaveImageTask(Bitmap bitmap, String path, ISaveImageTask listener) {

        image = bitmap;
        mListener = listener;
        filePath = path;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        PCLUtils.deleteImageFromExternalStorage(filePath);

        try {
            String name = new File(filePath).getName();
            String baseUr = new File(filePath).getParentFile().toString();
            File myDir = new File(baseUr);
            myDir.mkdirs();
            File file = new File(myDir,   name);
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            return true;

        } catch (IOException ignored) {

        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean successful) {
        if (image != null) {
            image.recycle();
            image = null;
        }

        if (successful)
            mListener.onSuccessfulSave();
        else
            mListener.onSaveFailure();
    }

    public interface ISaveImageTask {
        void onSuccessfulSave();

        void onSaveFailure();
    }
}
