package com.louisgeek.speech.interfaces;

import java.util.List;

/**
 * Created by louisgeek on 2017/12/27.
 */

public interface ISpeechSynthesizer {

    void init(boolean isSpeakEnAsPinyin, MySpeechSynthesizerListener mySpeechSynthesizerListener);

    void speak(String text);

    void speak(int textResid);

    void batchSpeak(List<String> texts);

    void synthesize(String text);

    void pause();

    void resume();

    void stop();

    void cancel();

    void release();

    void destroy();

    void goProgress(float progress);

    void setSpeechSpeed(float speed);
}
