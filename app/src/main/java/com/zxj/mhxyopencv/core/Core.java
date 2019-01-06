package com.zxj.mhxyopencv.core;

import android.content.Context;

import com.zxj.mhxyopencv.StatusListener;
import com.zxj.mhxyopencv.utils.file.CopyFile;
import com.zxj.mhxyopencv.utils.file.Zip;

import java.io.File;

public class Core {
    private StatusListener listener;

    public void setListener(StatusListener listener) {
        this.listener = listener;
    }
    public void start(Context context){
        prepareRes(context);
    }
    //准备资源
    private void prepareRes(Context context){
        File file = CopyFile.copyFileFromAssets(context, "res.zip", new File(context.getCacheDir(), "res1.zip").getAbsolutePath());
        if (file != null){
            listener.copyRes(true);
            String absolutePath = file.getAbsolutePath();
            try {
                Zip.UnZipFolder(absolutePath, new File(context.getCacheDir(), "").getAbsolutePath());
                listener.unzipRes(true);
            } catch (Exception e) {
                e.printStackTrace();
                listener.unzipRes(false);
            }
        }else {
            listener.copyRes(false);
        }
    }
}
