package com.louisgeek.speech.helper;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.louisgeek.speech._LibraryProvider;

import java.util.Locale;

/**
 * Created by louisgeek on 2018/3/2.
 * <p>
 * 逻辑上的 Internal storage:
 * 1 总是可用的
 * 2 这里的文件默认只能被我们的 app 所访问。
 * 3 当用户卸载app的时候，系统会把 internal 内该 app 相关的文件都清除干净。
 * 4 Internal 是我们在想确保不被用户与其他 app 所访问的最佳存储区域。
 * <p>
 * data 目录
 * <p>
 * 这下面的文件会在用户卸载我们的 app 时被系统删除
 * ROM 机身存储空间
 */
public class _LogicalInternalStorageHelper {
    private static final String TAG = "LogicalInternalStorageH";
    /**
     * /cache
     */
    public static String getDownloadCacheDirectoryPath() {
        return Environment.getDownloadCacheDirectory().getAbsolutePath();
    }


    /**
     * /system
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * /data
     */
    public static String getDataDirectoryPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * app 的 internal 缓存目录
     * <p>
     * /data/data/<application package>/cache
     * miui等系统应用多开 时候
     * /data/user/0/<application package>/cache
     * miui等系统应用多开 时候 小号
     * /data/user/999/<application package>/cache
     */
    public static String getCacheDirPath() {
        Context appContext = _LibraryProvider.provideAppContext();
        return appContext.getCacheDir().getAbsolutePath();
    }

    /**
     * app 的 internal 目录
     * <p>
     * /data/data/<application package>/files
     * <p>
     * miui等系统应用多开 时候
     * /data/user/0/<application package>/files
     * miui等系统应用多开 时候 小号
     * /data/user/999/<application package>/files
     */
    public static String getFilesDirPath() {
        Context appContext = _LibraryProvider.provideAppContext();
        return appContext.getFilesDir().getAbsolutePath();
    }


    public static String getInternalStorageTotalSpaceFixed() {
        float mb = getInternalStorageTotalSpace() * 1.0f / 1024 / 1024 / 1024;
        return String.format(Locale.CHINA, "%.2f", mb);
    }

    public static long getInternalStorageTotalSpace() {
        String path = getDataDirectoryPath();
        StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getTotalBytes();
        } else {
            return statFs.getBlockSizeLong() * statFs.getBlockCountLong();
        }
    }

    public static String getInternalStorageAvailableSpaceFixed() {
        float gb = getInternalStorageAvailableSpace() * 1.0f / 1024 / 1024 / 1024;
        return String.format(Locale.CHINA, "%.2f", gb);
    }

    public static long getInternalStorageAvailableSpace() {
        String path = getDataDirectoryPath();
        StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // getAvailableBytes 比 getFreeBytes 准确
            return statFs.getAvailableBytes();
        } else {
            // getAvailableBlocksLong 比 getFreeBlocksLong 准确
            return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        }
    }
}
