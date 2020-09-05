package com.louisgeek.speech.tool;

import android.util.Log;

import com.louisgeek.speech._LibraryProvider;


/**
 * Created by louisgeek on 2018/8/5.
 */
public class _LogTool {
    private static final int LOG_VERBOSE = 0;//所有显示
    private static final int LOG_DEBUG = 1;
    private static final int LOG_INFO = 2;
    private static final int LOG_WARN = 3;
    private static final int LOG_ERROR = 4;
    private static final int LOG_NONE = Integer.MAX_VALUE;//所有不显示
    private static final String TAG = "LogTool";
    //    private static int mLogLevel = LogTool.LOG_NONE;
    private static int mLogLevel = _LibraryProvider.provideDebug() ? LOG_VERBOSE : LOG_NONE;

    public static void d(String tag, String msg) {
        if (mLogLevel <= LOG_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (mLogLevel <= LOG_INFO) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (mLogLevel <= LOG_WARN) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (mLogLevel <= LOG_ERROR) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (mLogLevel <= LOG_ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }
}
