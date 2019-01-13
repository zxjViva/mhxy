package com.zxj.mhxyopencv.core;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResFilePathConstants {
    public static final String TAG = "ResFilePathConstants";
    public static final String resFilePath = new File(
            Environment.getExternalStorageDirectory().getAbsolutePath(),"mhxyOpencvRes").getAbsolutePath();
    public static final String tempResFilePath = new File(
            Environment.getExternalStorageDirectory().getAbsolutePath(),"mhxyOpencvResTemp").getAbsolutePath();
    private static Map<String,String> resMap = new LinkedHashMap<>();
    static {
        File tempDir = new File(tempResFilePath);
        if (!tempDir.exists()){
            tempDir.mkdirs();
        }
        resMap.put("key_fighting",resFilePath+ "/keyRes/key_fighting.png");
        resMap.put("key_Autofight",resFilePath+ "/keyRes/key_autoFight.png");
    }
    public static String getKeyFightingPath(){
        String path = resMap.get("key_fighting");
        Log.e(TAG, "getKeyFightingPath: " + path );
        return path;
    }
    public static String getKeyAutoFightPath(){
        String path = resMap.get("key_Autofight");
        Log.e(TAG, "getKeyAutoFightPath: " + path );
        return path;
    }
}
