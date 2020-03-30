package com.louisgeek.speech.impls;

import com.louisgeek.speech.interfaces.ISpeechSynthesizer;
import com.louisgeek.speech.interfaces.MySpeechSynthesizerListener;

import java.util.List;

/**
 * Created by louisgeek on 2018/11/26.
 */
public class EmptySoundSpeechSynthesizer implements ISpeechSynthesizer {

    @Override
    public void init(boolean isSpeakEnAsPinyin, MySpeechSynthesizerListener mySpeechSynthesizerListener) {

    }


    @Override
    public void speak(String text) {

    }

    @Override
    public void speak(int textResid) {

    }

    @Override
    public void batchSpeak(List<String> texts) {

    }

    @Override
    public void synthesize(String text) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void release() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void goProgress(float progress) {

    }

    @Override
    public void setSpeechSpeed(float speed) {

    }
}
