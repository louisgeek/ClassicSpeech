package com.louisgeek.speech;


import com.louisgeek.speech.impls.UniSoundSpeechSynthesizer;
import com.louisgeek.speech.interfaces.ISpeechSynthesizer;

/**
 * Created by louisgeek on 2017/12/27.
 */

public class SpeechSynthesizerFactory {

    public static ISpeechSynthesizer getInstance() {
        return Inner.INSTANCE;
    }

    /**
     * 静态内部类实现单例
     */
    private static class Inner {
                private static final ISpeechSynthesizer INSTANCE = create(UniSoundSpeechSynthesizer.class);
//        private static final ISpeechSynthesizer INSTANCE = create(EmptySoundSpeechSynthesizer.class);
    }

    //=========================================
    private static <T extends ISpeechSynthesizer> T create(Class<T> tClass) {
        ISpeechSynthesizer base = null;
        try {
            base = (ISpeechSynthesizer) Class.forName(tClass.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) base;
    }
}
