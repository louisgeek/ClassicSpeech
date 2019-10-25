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

    /**
     * 不考虑物理外置存储
     *
     * @param childSavePath
     * @return
     */
    public static String getSavePath(String childSavePath) {
        String savePath = null;
        if (_LogicalExternalStorageHelper.isExternalStorageWritable()) {
            //存在逻辑外置存储（模拟外置存储+真实外置存储）
            //模拟外置存储 剩余空间
            long externalStorageAvailableSpace = _LogicalExternalStorageHelper.getExternalStorageAvailableSpace();
            Log.e(TAG, "模拟外置存储剩余空间:MB: " + _LogicalExternalStorageHelper.getExternalStorageAvailableSpace_MB());
            Log.e(TAG, "模拟外置存储剩余空间:GB: " + _LogicalExternalStorageHelper.getExternalStorageAvailableSpace_GB());
            if (externalStorageAvailableSpace > INSTALL_NEED_LEFT_SPACE_IN_MB * 1024 * 1024) {
                savePath = _LogicalExternalStorageHelper.getExternalStorageDirectoryPath() + File.separator + childSavePath + File.separator;
            }
            //不处理 物理外置存储
        }
        if (savePath == null) {
            long internalStorageAvailableSpace = _LogicalInternalStorageHelper.getInternalStorageAvailableSpace();
            Log.e(TAG, "模拟内置存储剩余空间:MB: " + _LogicalInternalStorageHelper.getInternalStorageAvailableSpace_MB());
            Log.e(TAG, "模拟内置存储剩余空间:GB: " + _LogicalInternalStorageHelper.getInternalStorageAvailableSpace_GB());
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
