package com.brainx.bxcroplibrary.BXAsyncTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tahir zahoor on 07/07/2017.
 */

public class BXSaveImageTask extends AsyncTask<Void, Void, Boolean> {


    private String filePath;
    private Bitmap image;
    private ISaveImageTask mListener;

    public BXSaveImageTask(Bitmap bitmap, String path, ISaveImageTask listener) {

        image = bitmap;
        mListener = listener;
        filePath = path;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String name = new File(filePath).getName();
            String baseUr = new File(filePath).getParentFile().toString();
            File myDir = new File(baseUr);
            myDir.mkdirs();
            String[] namelist = name.split("\\.(?=[^\\.]+$)");
            File file = new File(myDir, namelist[0] + ".jpg");
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            filePath=file.getPath();
            return true;

        } catch (IOException ignored) {
            ignored.printStackTrace();
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
            mListener.onSuccessfulSave(filePath);
        else
            mListener.onSaveFailure();
    }

    public interface ISaveImageTask {
        void onSuccessfulSave(String filePath);

        void onSaveFailure();
    }
}
