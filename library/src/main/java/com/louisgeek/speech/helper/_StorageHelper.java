package com.louisgeek.speech.helper;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.louisgeek.speech._LibraryProvider;

import java.io.File;

/**
 * Created by louisgeek on 2018/11/12.
 */
 public class _StorageHelper {
    private static final String TAG = "StorageHelper";
    private static final int INSTALL_NEED_LEFT_SPACE_IN_MB = 100;//100 MB

    public static String getSavePath(String childSavePath) {
        String savePath = null;
        if (_LogicalExternalStorageHelper.isExternalStorageWritable()) {
            long externalStorageAvailableSpace = _LogicalExternalStorageHelper.getExternalStorageAvailableSpace();
            Log.e(TAG, "getSavePath: externalStorageAvailableSpace " + externalStorageAvailableSpace);

            Log.e(TAG, "getSavePath:mb: " + externalStorageAvailableSpace * 1.0f / 1024 / 1024);
            Log.e(TAG, "getSavePath:gb: " + externalStorageAvailableSpace * 1.0f / 1024 / 1024 / 1024);
            if (externalStorageAvailableSpace > INSTALL_NEED_LEFT_SPACE_IN_MB * 1024 * 1024) {
                savePath = _LogicalExternalStorageHelper.getExternalCacheDirPath() + File.separator + childSavePath + File.separator;
            }
        }
        if (savePath == null) {
            long internalStorageAvailableSpace = _LogicalInternalStorageHelper.getInternalStorageAvailableSpace();
            Log.e(TAG, "getSavePath:mb: " + internalStorageAvailableSpace * 1.0f / 1024 / 1024);
            Log.e(TAG, "getSavePath:gb: " + internalStorageAvailableSpace * 1.0f / 1024 / 1024 / 1024);
            if (internalStorageAvailableSpace > INSTALL_NEED_LEFT_SPACE_IN_MB * 1024 * 1024) {
                savePath = _LogicalInternalStorageHelper.getCacheDirPath() + File.separator + childSavePath + File.separator;
            }
        }
        if (savePath == null) {
            Context appContext = _LibraryProvider.provideAppContext();
            Toast.makeText(appContext, "存储容量不足" + INSTALL_NEED_LEFT_SPACE_IN_MB + "MB", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "getSavePath: 存储容量不足" + INSTALL_NEED_LEFT_SPACE_IN_MB + " MB");
        }
        return savePath;
    }
}
