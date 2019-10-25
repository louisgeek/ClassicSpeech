package com.louisgeek.speech;

import android.content.Context;

/**
 * Created by louisgeek on 2018/9/20.
 */
public class _LibraryProvider {
    private static Context mAppContext;
    private static boolean mDebug;

    /**
     * _LibraryInitContentProvider 调用
     * @param context
     */
    public static void initAppContext(Context context) {
        mAppContext = context.getApplicationContext();
    }
    public static void initDebug(boolean debug) {
        mDebug = debug;
    }
    public static Context provideAppContext() {
        return mAppContext;
    }

    public static boolean provideDebug() {
        return mDebug;
    }

}
