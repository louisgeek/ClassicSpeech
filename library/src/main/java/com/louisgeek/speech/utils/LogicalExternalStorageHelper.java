package com.louisgeek.speech.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.louisgeek.speech.LibraryProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by louisgeek on 2018/3/2.
 * 逻辑上的 External storage
 * LogicalExternalStorage =  PhysicalInternalStorage + PhysicalExternalStorage
 * 1 并不总是可用的，因为用户有时会通过USB存储模式挂载外部存储器，当取下挂载的这部分后，就无法对其进行访问了。
 * 2 是大家都可以访问的，因此保存在这里的文件可能被其他程序访问。
 * 3 当用户卸载我们的app时，系统仅仅会删除external根目录（getExternalFilesDir()、getExternalCacheDir()）下的相关文件。
 * 4 External是在不需要严格的访问权限并且希望这些文件能够被其他app所共享或者是允许用户通过电脑访问时的最佳存储区域。
 * <p>
 * 包括 物理上的内置存储卡（ 模拟存储 ）和外置存储卡（内存卡 /Micro SD卡 / TF卡）
 * 需要
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */

public class LogicalExternalStorageHelper {
    private static final String TAG = "LogicalExternalStorageT";

    /**
     * read and write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * at least read
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public static boolean isPhysicalExternalStorageWritable(Context context, String path) {
        try {
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method method = StorageManager.class.getMethod("getVolumeState", String.class);
            String state = (String) method.invoke(sm, path);
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "isPhysicalExternalStorageWritable: ", e);
        }
        return false;
    }

    public static boolean isPhysicalExternalStorageReadable(String path) {
        Context appContext = LibraryProvider.provideAppContext();
        try {
            StorageManager sm = (StorageManager) appContext.getSystemService(Context.STORAGE_SERVICE);
            Method method = StorageManager.class.getMethod("getVolumeState", String.class);
            String state = (String) method.invoke(sm, path);
            if (state.equals(Environment.MEDIA_MOUNTED) ||
                    state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "isPhysicalExternalStorageReadable: ", e);
        }
        return false;
    }

    /**
     * 获取所有包括 模拟存储 + sd/tf 卡
     */
    private static String[] getLogicalExternalStoragePaths() {
        Context appContext = LibraryProvider.provideAppContext();
        try {
            StorageManager sm = (StorageManager) appContext.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            //获取所有sd卡路径
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm);
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getLogicalExternalStoragePaths: ", e);
        }
        return null;
    }


    /**
     * 获取内置内存卡 Physical Internal storage / Physical UnRemovable storage 路径
     * 例： /storage/emulated/0   【/mnt/m_internal_storage】
     * 或
     * /storage/sdcard0
     * <p>
     * {@link LogicalExternalStorageHelper#getExternalStorageDirectoryPath()}
     */
    public static String getPhysicalInternalStoragePath() {
        String[] paths = getLogicalExternalStoragePaths();
        if (paths != null && paths.length > 0) {
            //第一个 模拟存储
            return paths[0];
        }
        return null;
    }

    /**
     * //获取其他所有可用 外置内存卡 Physical External storage / Physical Removable storage 路径 集合
     * 可能是/mnt/sdcard、/mnt/extsd、/mnt/external_sd 、/mnt/sdcard2
     */
    public static List<String> getPhysicalExternalStoragePathList() {
        String[] paths = getLogicalExternalStoragePaths();
        //从第二张开始  内存卡 /Micro SD卡 / TF卡
        if (paths != null && paths.length >= 1) {
            List<String> sdPathList = new ArrayList<>();
            for (int i = 1; i < paths.length; i++) {
                if (isPhysicalExternalStorageReadable(paths[i])) {
                    sdPathList.add(paths[i]);
                }
            }
            return sdPathList;
        }
        return null;
    }


    /**
     * 获取第一个外置内存卡 External storage / Removable storage 路径
     * 可能是/mnt/sdcard、/mnt/extsd、/mnt/external_sd 、/mnt/sdcard2
     * <p>
     * 例：
     * /storage/sdcard1  【/mnt/m_external_sd】
     * 真机：
     * /storage/sdcard1     华为 M2-A01W
     * /storage/E8C3-9C76   红米 NOTE 4X
     * /storage/6279-371D   Galaxy Tab S2  SM-T819C
     * /storage/0403-0201   华为 nova 2s
     */
    public static String getPhysicalExternalStoragePath() {
        List<String> removableStoragePathList = getPhysicalExternalStoragePathList();
        if (removableStoragePathList != null && removableStoragePathList.size() > 0) {
            return removableStoragePathList.get(0);
        }
        return null;
    }


    /**
     * Public files
     * <p>
     * /storage/emulated/0/Download  【"mnt/sdcard/Download"】
     * 或
     * /storage/sdcard0/Download
     * <p>
     * 其他
     * Environment.DIRECTORY_MUSIC 音乐
     * Environment.DIRECTORY_PODCASTS 音/视频的剪辑片段
     * Environment.DIRECTORY_ALARMS  闹钟的声音
     * Environment.DIRECTORY_RINGTONES  铃声
     * Environment.DIRECTORY_NOTIFICATIONS
     * Environment.DIRECTORY_PICTURES 图片
     * Environment.DIRECTORY_MOVIES 电影
     * Environment.DIRECTORY_DOWNLOADS 下载的内容
     * Environment.DIRECTORY_DCIM 拍摄的照片/视频
     * Environment.DIRECTORY_DOCUMENTS 文档
     *
     * @return
     */
    public static String getExternalStoragePublicDirectory_DirectoryDownloads() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取内置存储卡路径
     * /storage/emulated/0
     * 或
     * /storage/sdcard0
     *
     * @return
     */
    public static String getExternalStorageDirectoryPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * /storage/emulated/0/Android/data/<application package>/cache
     * 或
     * /storage/sdcard0/Android/data/<application package>/cache
     * Private files ：缓存文件
     * 注意：这下面的文件会在用户卸载我们的app时被系统删除
     * 对应 设置->应用->应用详情里面的"清除缓存"选项
     *
     * @return
     */
    public static String getExternalCacheDirPath() {
        Context appContext = LibraryProvider.provideAppContext();
        return appContext.getExternalCacheDir().getAbsolutePath();
    }

    /**
     * /storage/emulated/0/Android/data/<application package>/files/Download
     * 或
     * /storage/sdcard0/Android/data/<application package>/files/Download
     * Private  files ：数据文件
     * 注意：这下面的文件会在用户卸载我们的app时被系统删除
     * 对应 设置->应用->应用详情里面的"清除数据"选项
     * 其他
     * Environment.DIRECTORY_MUSIC 音乐
     * Environment.DIRECTORY_PODCASTS 音/视频的剪辑片段
     * Environment.DIRECTORY_ALARMS  闹钟的声音
     * Environment.DIRECTORY_RINGTONES  铃声
     * Environment.DIRECTORY_NOTIFICATIONS
     * Environment.DIRECTORY_PICTURES 图片
     * Environment.DIRECTORY_MOVIES 电影
     * Environment.DIRECTORY_DOWNLOADS 下载的内容
     * Environment.DIRECTORY_DCIM 拍摄的照片/视频
     * Environment.DIRECTORY_DOCUMENTS 文档
     *
     * @return
     */
    public static String getExternalFilesDirPath_DirectoryDownloads() {
        Context appContext = LibraryProvider.provideAppContext();
        return appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * /storage/emulated/0/Android/data/<application package>/files
     * 或
     * /storage/sdcard0/Android/data/<application package>/files
     * Private  files ：数据文件
     * 注意：这下面的文件会在用户卸载我们的app时被系统删除
     * 对应 设置->应用->应用详情里面的"清除数据"选项
     */
    public static String getExternalFilesDirPath() {
        Context appContext = LibraryProvider.provideAppContext();
        return appContext.getExternalFilesDir(null).getAbsolutePath();
    }

    public static String getExternalStorageTotalSpaceFixed() {
        //获取 模拟存储
        float gb = getExternalStorageTotalSpace() * 1.0f / 1024 / 1024 / 1024;
        return String.format(Locale.CHINA, "%.2f", gb);
    }

    public static long getExternalStorageTotalSpace() {
        //获取 模拟存储
        String path = getPhysicalInternalStoragePath();
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getTotalBytes();
        } else {
            return statFs.getBlockSizeLong() * statFs.getBlockCountLong();
        }
    }

    public static String getExternalStorageAvailableSpaceFixed() {
        //获取 模拟存储
        float mb = getExternalStorageAvailableSpace() * 1.0f / 1024 / 1024 / 1024;
        return String.format(Locale.CHINA, "%.2f", mb);
    }

    public static long getExternalStorageAvailableSpace() {
        //获取 模拟存储
        String path = getPhysicalInternalStoragePath();
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
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
