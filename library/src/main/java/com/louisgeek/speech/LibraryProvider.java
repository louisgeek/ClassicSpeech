package com.louisgeek.speech;

import android.content.Context;

/**
 * Created by louisgeek on 2018/9/20.
 */
public class LibraryProvider {
    private static Context mAppContext;

    static void init(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static Context provideAppContext() {
        return mAppContext;
    }

}
