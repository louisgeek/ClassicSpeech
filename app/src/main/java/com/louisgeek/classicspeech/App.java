package com.louisgeek.classicspeech;

import android.app.Application;

import com.louisgeek.speech.SpeechSynthesizerFactory;

/**
 * Created by louisgeek on 2019/10/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //
        SpeechSynthesizerFactory.getInstance().init(false, null);
    }
}
