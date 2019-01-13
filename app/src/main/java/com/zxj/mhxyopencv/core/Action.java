package com.zxj.mhxyopencv.core;

import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.zxj.mhxyopencv.core.model.CoordModel;

import java.io.File;
import java.util.List;

public class Action {
    private static final String TAG = "Action";
    private static UiDevice uiDevice;
    public static File screenCut(){
        File file = new File(ResFilePathConstants.tempResFilePath, System.currentTimeMillis()+".png");
        Log.e(TAG, "screenCut file path : "  + file.getAbsolutePath() );
        getUiDevice().takeScreenshot(file);
        return file;
    }

    public static void click(CoordModel coordModel){
        Log.e(TAG, "click: (" + coordModel.x + "," + coordModel.y + ")" );
        getUiDevice().click(coordModel.x,coordModel.y);
    }

    public static void click(List<CoordModel> points){
        Point[] pp = new Point[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point point = new Point();
            point.y = points.get(i).y;
            point.x = points.get(i).x;
            pp[i] = point;
        }
        getUiDevice().swipe(pp,40);
    }

    public static UiDevice getUiDevice(){
        return uiDevice == null ? UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) : uiDevice;
    }
}
