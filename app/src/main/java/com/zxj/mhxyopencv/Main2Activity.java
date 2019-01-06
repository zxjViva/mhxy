package com.zxj.mhxyopencv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zxj.mhxyopencv.utils.file.CopyFile;
import com.zxj.mhxyopencv.utils.file.Zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main2Activity extends AppCompatActivity implements Runnable,StatusListener {
    private final String TAG = "zxj1";
    private TextView stateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        stateTv = findViewById(R.id.state);
        File file = CopyFile.copyFileFromAssets(this, "res.zip", new File(getCacheDir(), "res1.zip").getAbsolutePath());
        if (file != null){
            stateTv.setText("复制资源文件成功");
            String absolutePath = file.getAbsolutePath();
            try {
                Zip.UnZipFolder(absolutePath, new File(getCacheDir(), "").getAbsolutePath());
                stateTv.setText("解压资源文件成功");
            } catch (Exception e) {
                e.printStackTrace();
                stateTv.setText("解压资源文件失败");
            }
        }else {
            stateTv.setText("复制资源文件失败");
        }

        //        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            File file = copyDirs2Cache(this,"res",getAssets().open("res"));
            if (file != null){
                String absolutePath = file.getAbsolutePath();
                Log.e("zxj1", "run: " + absolutePath);
                int length = absolutePath.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copyRes(boolean successed) {

    }
    @Override
    public void unzipRes(boolean successed) {

    }
    public File copyDirs2Cache(Context context,String fileName,InputStream is){
        File targetFile = new File(context.getCacheDir(), fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[2048];
            int readCount;
            while ((readCount = is.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,readCount);
            }
            fileOutputStream.flush();
            is.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            targetFile = null;
        }
        return targetFile;

    }
}
