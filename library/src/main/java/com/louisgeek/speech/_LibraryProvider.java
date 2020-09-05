package com.louisgeek.speech;

import android.content.Context;

/**
 * Created by louisgeek on 2018/9/20.
 */
public class _LibraryProvider {

    private static Context mAppContext;
    private static boolean mDebug;

    /**
     * @param context
     */
    public static void initAppContext(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static Context provideAppContext() {
        return mAppContext;
    }

    public static void initDebug(boolean debug) {
        mDebug = debug;
    }

    public static boolean provideDebug() {
        return mDebug;
    }

    //======================================================
    private static volatile _LibraryProvider sInstance;

    private _LibraryProvider() {
    }

    public static _LibraryProvider getInstance() {
        if (sInstance == null) {
            synchronized (_LibraryProvider.class) {
                if (sInstance == null) {
                    sInstance = new _LibraryProvider();
                }
            }
        }
        return sInstance;
    }
    //======================================================
}
