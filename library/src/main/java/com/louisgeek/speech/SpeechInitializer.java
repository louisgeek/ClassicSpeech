package com.louisgeek.speech;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * startup
 */
public class SpeechInitializer implements Initializer<_LibraryProvider> {
    @NonNull
    @Override
    public _LibraryProvider create(@NonNull Context context) {
        _LibraryProvider.initAppContext(context);
        return _LibraryProvider.getInstance();
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
