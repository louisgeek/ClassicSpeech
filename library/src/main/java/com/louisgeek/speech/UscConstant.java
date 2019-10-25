package com.louisgeek.speech;


import com.louisgeek.speech.helper._StorageHelper;

import java.io.File;
import java.util.Locale;

public class UscConstant {
    private static final String TAG = "UscConstant";

    ////////////////////////////////KEY////////////////////////////////
    public static final String UNISOUND_YUYIN_APP_KEY = "cw5sxgbdanfahioe6f76cstzhujdnevmgyelzaaf";
    public static final String UNISOUND_YUYIN_APP_SECRET = "25fdb0a39396e63e01d4333a2ee4a6f0";
    //
    public static final String UniSoundTTSModel_frontend_model = "frontend_model";
    public static final String UniSoundTTSModel_backend_model = "backend_female";//普通
//    public static final String UniSoundTTSModel_backend_model = "backend_lzl";//林志玲 文件得加入 Assets

    public static String getUniSoundTTSModelsSdcardPath() {
//        "%s/common/tts/models/"
        return String.format(Locale.CHINA, "%s%scommon%stts%smodels%s", _StorageHelper.getSavePath("louisgeek"),
                File.separator, File.separator, File.separator, File.separator);
    }

    public static String getFrontendUniSoundTTSModelsAssetsPath() {
        return getUniSoundTTSModelsAssetsPath(UniSoundTTSModel_frontend_model);
    }

    public static String getBackendUniSoundTTSModelsAssetsPath() {
        return getUniSoundTTSModelsAssetsPath(UniSoundTTSModel_backend_model);
    }

    private static String getUniSoundTTSModelsAssetsPath(String uniSoundTTSModelName) {
        return String.format(Locale.CHINA, "UniSoundTTSModels%s%s", File.separator, uniSoundTTSModelName);
    }
}
