package com.louisgeek.speech.impls;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;

import com.louisgeek.speech.UscConstant;
import com.louisgeek.speech._LibraryProvider;
import com.louisgeek.speech.interfaces.ISpeechSynthesizer;
import com.louisgeek.speech.interfaces.MySpeechSynthesizerListener;
import com.louisgeek.speech.tool._AssetManagerTool;
import com.louisgeek.speech.tool._LogTool;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by louisgeek on 2017/12/27.
 */

public class UniSoundSpeechSynthesizer implements ISpeechSynthesizer {
    private static final String TAG = "UniSoundSpeechSynthesiz";
    private SpeechSynthesizer mSpeechPlayer;
    private Context mContext;
    private String mFrontendModelPath;
    private String mBackendModelPath;
    private int code;
    private int mSpeed = 50;
    private boolean isInited;
    private boolean mIsSpeakEnAsPinyin;
    private MySpeechSynthesizerListener mMySpeechSynthesizerListener;

    public void init(boolean isSpeakEnAsPinyin, MySpeechSynthesizerListener mySpeechSynthesizerListener) {
        mContext = _LibraryProvider.provideAppContext();
        mIsSpeakEnAsPinyin = isSpeakEnAsPinyin;
        mMySpeechSynthesizerListener = mySpeechSynthesizerListener;
        initOfflineModels();
    }

    private void initOfflineModels() {
        mFrontendModelPath = UscConstant.getUniSoundTTSModelsSdcardPath() + UscConstant.UniSoundTTSModel_frontend_model;
        mBackendModelPath = UscConstant.getUniSoundTTSModelsSdcardPath() + UscConstant.UniSoundTTSModel_backend_model;
        File frontendModelFile = new File(mFrontendModelPath);
        File backendModelFile = new File(mBackendModelPath);
        if (frontendModelFile.exists() && backendModelFile.exists()) {
            _LogTool.i(TAG, "离线模型文件已存在");
            initTts();
        } else {
            //拷贝文件
            Executors.newFixedThreadPool(2).execute(new Runnable() {
                @Override
                public void run() {
                    String file_frontend = _AssetManagerTool.copyAssetsFile(mContext, UscConstant.getUniSoundTTSModelsSdcardPath(), UscConstant.UniSoundTTSModel_frontend_model
                            , UscConstant.getFrontendUniSoundTTSModelsAssetsPath());
                    String file_backend = _AssetManagerTool.copyAssetsFile(mContext, UscConstant.getUniSoundTTSModelsSdcardPath(), UscConstant.UniSoundTTSModel_backend_model
                            , UscConstant.getBackendUniSoundTTSModelsAssetsPath());
                    if (file_frontend != null && file_backend != null) {
                        //拷贝成功
                        initTts();
                    } else {
                        _LogTool.e(TAG, "run: 拷贝失败");
                    }

                }
            });
          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    String file_frontend = _AssetManagerTool.copyAssetsFile(mContext, UscConstant.FILE_PATH_TTS_UNISOUND_UniSoundTTSModels, UscConstant.FILE_UNISOUND_FRONTEND_MODEL
                            , UscConstant.ASSETS_FILE_PATH_UNISOUND_FRONTEND_MODEL);
                    String file_backend = _AssetManagerTool.copyAssetsFile(mContext, UscConstant.FILE_PATH_TTS_UNISOUND_UniSoundTTSModels, UscConstant.FILE_UNISOUND_BACKEND_MODEL
                            , UscConstant.ASSETS_FILE_PATH_UNISOUND_BACKEND_MODEL);
                    if (file_frontend != null && file_backend != null) {
                        initTts();
                    }
                }
            }).start();*/
        }
    }

    private void initTts() {
        // 初始化语音合成对象
        mSpeechPlayer = new SpeechSynthesizer(mContext, UscConstant.UNISOUND_YUYIN_APP_KEY, UscConstant.UNISOUND_YUYIN_APP_SECRET);
        // 设置本地合成
        mSpeechPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);//TTS_SERVICE_MODE_NET
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, mFrontendModelPath);// 设置前端模型
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, mBackendModelPath);// 设置后端模型
        //
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, mSpeed);//语速 范围 0 ~ 100
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_PITCH, 50);//音高 范围 0 ~ 100  大而尖锐 普通女生推荐 40  林志玲推荐
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 90);//音量 范围 0 ~ 100

        // mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_SAMPLE_RATE, 16 * 1000);//合成码率 例如：16 * 1000
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_STREAM_TYPE, AudioManager.STREAM_MUSIC);//合成采样率 例如：AudioManager.STREAM_MUSIC
        //  mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_PLAY_START_BUFFER_TIME, 10);//设置播放开始缓冲时间 0 ~ 500 单位
        //mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_IS_READ_ENLISH_IN_PINYIN, true);//设置是否将英文按拼音读 如：wang->王
//        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_IS_DEBUG, false);//boolean 设置是否将英文按拼音读
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_IS_READ_ENLISH_IN_PINYIN, mIsSpeakEnAsPinyin);//设置是否将英文按拼音读 如：wang->王
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_FRONT_SILENCE, 10);//语音开始段的静音时长 0 ~ 1000 单位 ms
        mSpeechPlayer.setOption(SpeechConstants.TTS_KEY_BACK_SILENCE, 100);//语音结尾段的静音时长 0 ~ 1000 单位 ms
        // 设置回调监听
        mSpeechPlayer.setTTSListener(new SpeechSynthesizerListener() {

            @Override
            public void onEvent(int type) {
                switch (type) {
                    case SpeechConstants.TTS_EVENT_INIT:
                        // 初始化成功回调
                        _LogTool.d(TAG, "onInitFinish");
                        isInited = true;
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onInit();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                        // 开始合成回调
                        _LogTool.d(TAG, "beginSynthesizer");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onSynthesizerStart();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                        // 合成结束回调
                        _LogTool.d(TAG, "endSynthesizer");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onSynthesizerEnd();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                        // 开始缓存回调
                        _LogTool.d(TAG, "beginBuffer");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onBufferBegin();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
                        // 缓存完毕回调
                        _LogTool.d(TAG, "bufferReady");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onBufferReady();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_START:
                        // 开始播放回调
                        _LogTool.d(TAG, "onPlayBegin");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onPlayStart();
                        }
                        mStatus = "onPlayBegin";
                        initTask();
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_END:
                        // 播放完成回调
                        _LogTool.d(TAG, "onPlayEnd");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onPlayEnd();
                        }
                        mStatus = "onPlayEnd";

                        break;
                    case SpeechConstants.TTS_STATUS_END:
                        // 播放完成回调2
                        _LogTool.d(TAG, "onEnd");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onEnd();
                        }
                        mStatus = "onEnd";

                        break;
                    case SpeechConstants.TTS_EVENT_PAUSE:
                        // 暂停回调
                        _LogTool.d(TAG, "pause");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onPause();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_RESUME:
                        // 恢复回调
                        _LogTool.d(TAG, "resume");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onResume();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_STOP:
                        // 停止回调
                        _LogTool.d(TAG, "stop");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onStop();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_RELEASE:
                        // 释放资源回调
                        _LogTool.d(TAG, "release");
                        if (mMySpeechSynthesizerListener != null) {
                            mMySpeechSynthesizerListener.onRelease();
                        }
                        break;
                    default:
                        _LogTool.d(TAG, "TTS others:" + type);
                        break;
                }
            }

            @Override
            public void onError(int type, String errorMSG) {
                _LogTool.e(TAG, "TTS type:" + type);
                _LogTool.e(TAG, "TTS errorMSG:" + errorMSG);
                if (mMySpeechSynthesizerListener != null) {
                    mMySpeechSynthesizerListener.onError(type, errorMSG);
                }
            }
        });
        // 初始化合成引擎
        try {
            code = mSpeechPlayer.init(null);
            log_d("code:" + code);
        } catch (Exception e) {
            log_d("Exception:" + e.getMessage());
            if (mMySpeechSynthesizerListener != null) {
                mMySpeechSynthesizerListener.onError(-999, e.getMessage());
            }
        }

    }

    private String mStatus;

    private void initTask() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                _LogTool.e(TAG, "run:status " + mSpeechPlayer.getStatus());

                if (mSpeechPlayer.getStatus() == SpeechConstants.TTS_STATUS_PLAYING) {
                    //  2 秒     3.5
                    //  7
                    //  50
                    //  ==========
                    //2分8秒  128  4.453125
                    //570
                    //50
                    //
                    //5324  884

                    _LogTool.e(TAG, "run:inin ");
                    int voice_speed = (int) mSpeechPlayer.getOption(SpeechConstants.TTS_KEY_VOICE_SPEED);
//                    int front_silence = (int) mSpeechPlayer.getOption(SpeechConstants.TTS_KEY_FRONT_SILENCE);//ms
//                    _LogTool.e(TAG, "run:front_silence " + front_silence);
//                    int back_silence = (int) mSpeechPlayer.getOption(SpeechConstants.TTS_KEY_BACK_SILENCE);//ms
//                    _LogTool.e(TAG, "run:back_silence " + back_silence);
//                    float speedTTT = (1.0f * 570 / 128) * (1.0f * voice_speed / 100);
                    // 1秒钟 4个字
                    mReadedTextLength += (1.0f * 560 / 128) * period;
//                    mReadedTextLength += (1.0f * 5324 / 884) * period;
                    _LogTool.e(TAG, "run: mReadedTextLength" + mReadedTextLength);
                    float progress = mReadedTextLength / mTotalText.length() * 100;
                    progress = mSpeed / 50 * progress;
                    progress = progress > 100 ? 100 : progress;
                    _LogTool.e(TAG, "run: progress" + progress);
                    if (mMySpeechSynthesizerListener != null) {
                        mMySpeechSynthesizerListener.onProgress(progress);
                    }
                } else if (mSpeechPlayer.getStatus() == SpeechConstants.TTS_STATUS_END) {
                    if (mScheduledExecutorService != null) {
                        mScheduledExecutorService.shutdownNow();
                        mReadedTextLength = 0;
                    }
                }
                if (mMySpeechSynthesizerListener != null) {
                    mMySpeechSynthesizerListener.onSpeechStatus(mSpeechPlayer.getStatus());
                }
            }
        }, 0, period, TimeUnit.SECONDS);
    }

    //多久获取一次进度值
    private int period = 1;
    private String mTotalText;
    private float mReadedTextLength;
    private ScheduledExecutorService mScheduledExecutorService;

    @Override
    public void speak(String text) {
        if (!isInited) {
            _LogTool.e(TAG, "语音组件尚未初始化完成，请稍后重试！");
            return;
        }
        if (TextUtils.isEmpty(text)) {
            log_d("text isEmpty");
            return;
        }
        _LogTool.e(TAG, "speak:length " + text.length());
        code = mSpeechPlayer.playText(text);
        log_d("code:" + code);
        mTotalText = text;
    }


    @Override
    public void speak(int textResid) {
        if (!isInited) {
            _LogTool.e(TAG, "语音组件尚未初始化完成，请稍后重试！");
            return;
        }
        if (textResid <= 0) {
            log_d("textResid is Error");
            return;
        }
        code = mSpeechPlayer.playText(mContext.getString(textResid));
        log_d("code:" + code);
        mTotalText = mContext.getString(textResid);
    }

    @Override
    public void batchSpeak(List<String> texts) {
        if (!isInited) {
            _LogTool.e(TAG, "语音组件尚未初始化完成，请稍后重试！");
            return;
        }
        if (texts != null) {
            log_d("texts is null");
            return;
        }
        for (String text : texts) {
            speak(text);
        }
    }

    @Override
    public void synthesize(String text) {
        if (TextUtils.isEmpty(text)) {
            log_d("text isEmpty");
            return;
        }
        mSpeechPlayer.synthesizeText(text);
    }

    @Override
    public void pause() {
        if (mSpeechPlayer != null) {
            mSpeechPlayer.pause();
        }
    }

    @Override
    public void resume() {
        if (mSpeechPlayer != null) {
            mSpeechPlayer.resume();
        }
    }

    @Override
    public void stop() {
        if (mSpeechPlayer != null) {
            mSpeechPlayer.stop();
        }
    }

    @Override
    public void cancel() {
        if (mSpeechPlayer != null) {
            code = mSpeechPlayer.cancel();
            mReadedTextLength = 0;
            log_d("code:" + code);
        }
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
        }
    }

    @Override
    public void release() {
        // 主动释放离线引擎
        if (mSpeechPlayer != null) {
            code = mSpeechPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
            log_d("code:" + code);
        }
    }

    @Override
    public void destroy() {
        if (mSpeechPlayer != null) {
            code = mSpeechPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
            log_d("code:" + code);
            mSpeechPlayer = null;
        }
    }

    @Override
    public void goProgress(float progress) {
        if (mTotalText == null) {
            return;
        }
        _LogTool.e(TAG, "goProgress:cancel " + progress);
        cancel();
        int needReadIndex = (int) (mTotalText.length() * progress / 100);
        mReadedTextLength = needReadIndex;
        String needRead = mTotalText.substring(needReadIndex);

        if (!isInited) {
            _LogTool.e(TAG, "语音组件尚未初始化完成，请稍后重试！");
            return;
        }
        if (TextUtils.isEmpty(needRead)) {
            log_d("needRead isEmpty");
            return;
        }
        _LogTool.e(TAG, "speak:length " + needRead.length());
        code = mSpeechPlayer.playText(needRead);
        log_d("code:" + code);
        //5324  884
        //2018-11-17 13:59:28.738 9461-9461/com.bsoft.mob.dw D/Log: onPlayEnd
        //2018-11-17 20:58:04.249 9461-9461/com.bsoft.mob.dw D/Log: onPlayBegin
        //2018-11-17 21:12:48.899 9461-9461/com.bsoft.mob.dw D/Log: onPlayEnd
//        2018-11-17 13:59:28.738 9461-9461/com.bsoft.mob.dw D/Log: onPlayEnd
//        2018-11-17 20:58:04.249 9461-9461/com.bsoft.mob.dw D/Log: onPlayBegin
//        2018-11-17 21:12:48.899 9461-9461/com.bsoft.mob.dw D/Log: onPlayEnd
    }

    @Override
    public void setSpeechSpeed(float speed) {
        cancel();
        mSpeechPlayer = null;

        mSpeed = (int) (speed * 50);
        initOfflineModels();
        speak(mTotalText);
    }


    //==============================================================================================
    private void log_d(String msg) {
        _LogTool.d(TAG, msg);
    }
}
