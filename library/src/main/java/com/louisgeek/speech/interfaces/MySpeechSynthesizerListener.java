package com.louisgeek.speech.interfaces;

/**
 * Created by louisgeek on 2018/11/15.
 */
public abstract class MySpeechSynthesizerListener {
    private static final String TAG = "MySpeechSynthesizerList";

    public void onInit() {
    }

    public void onSynthesizerStart() {
    }

    public void onSynthesizerEnd() {
    }

    public void onBufferBegin() {
    }

    public void onBufferReady() {
    }

    public void onPlayStart() {
    }

    public void onPlayEnd() {
    }

    public void onStop() {
    }

    public void onEnd() {
    }

    public void onRelease() {
    }

    public void onError(int type, String errorMSG) {
    }

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onProgress(float progress);

    public abstract void onSpeechStatus(int status);
}
