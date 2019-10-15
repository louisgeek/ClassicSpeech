package com.louisgeek.speech.utils;



import java.io.File;

public class UscConstant {
    private static final String TAG = "UscConstant";

    ////////////////////////////////KEY////////////////////////////////
    public static final String UNISOUND_YUYIN_APP_KEY = "cw5sxgbdanfahioe6f76cstzhujdnevmgyelzaaf";
    public static final String UNISOUND_YUYIN_APP_SECRET = "25fdb0a39396e63e01d4333a2ee4a6f0";


    ////////////////////////////////COMM////////////////////////////////
    private static final String FILE_PATH_SDCARD = StorageHelper.getSavePath("tts");

    private static final String FILE_PATH_BSOFT = FILE_PATH_SDCARD + "bsoft" + File.separator;
    private static final String FILE_PATH_IENR = FILE_PATH_BSOFT + "common" + File.separator;
    private static final String FILE_PATH_TTS = FILE_PATH_IENR + "tts" + File.separator;


    ///////////////////////////////UNISOUND/////////////////////////////////
    private static final String UNISOUND_BACKEND_MODEL_FEMALE = "backend_female";
    private static final String UNISOUND_BACKEND_MODEL_LZL = "backend_lzl";//林志玲
    //
    private static final String FILE_PATH_TTS_UNISOUND = FILE_PATH_TTS + "unisound" + File.separator;
    public static final String FILE_PATH_TTS_UNISOUND_UniSoundTTSModels = FILE_PATH_TTS_UNISOUND + "UniSoundTTSModels" + File.separator;
    //////文件名
    public static final String FILE_UNISOUND_FRONTEND_MODEL = "frontend_model";
    public static final String FILE_UNISOUND_BACKEND_MODEL = UNISOUND_BACKEND_MODEL_FEMALE; //普通
//    public static final String FILE_UNISOUND_BACKEND_MODEL = UNISOUND_BACKEND_MODEL_LZL; //林志玲

    //////Assets
    public static final String ASSETS_FILE_PATH_UNISOUND_FRONTEND_MODEL = "UniSoundTTSModels" + File.separator + FILE_UNISOUND_FRONTEND_MODEL;
    public static final String ASSETS_FILE_PATH_UNISOUND_BACKEND_MODEL = "UniSoundTTSModels" + File.separator + FILE_UNISOUND_BACKEND_MODEL;

}
