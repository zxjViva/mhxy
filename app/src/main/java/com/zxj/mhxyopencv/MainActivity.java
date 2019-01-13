package com.zxj.mhxyopencv;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private ImageView ivTarget;
    private ImageView ivSrc;
    private ImageView ivTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTarget = findViewById(R.id.target);
        ivSrc = findViewById(R.id.src);
        ivTemp = findViewById(R.id.temp);
        OpenCVLoader.initDebug();
        File srcFile = copyAssetAndWrite("zhandou.png");
        File targetFile = copyAssetAndWrite("key_fighting.png");
        try {
            ivSrc.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(srcFile)));
            ivTarget.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(targetFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mat g_tem = Imgcodecs.imread(targetFile.getAbsolutePath());
        Mat g_src = Imgcodecs.imread(srcFile.getAbsolutePath());
        // Example of a call to a native method
        int result_rows = g_src.rows() - g_tem.rows() + 1;
        int result_cols = g_src.cols() - g_tem.cols() + 1;
        Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(g_src, g_tem, g_result, Imgproc.TM_CCORR_NORMED);
        Core.normalize(g_result, g_result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);
        Point matchLocation = new Point();
        matchLocation = mmlr.maxLoc; // 此处使用maxLoc还是minLoc取决于使用的匹配算法
        Imgproc.rectangle(g_src, matchLocation,
                new Point(matchLocation.x + g_tem.cols(), matchLocation.y + g_tem.rows()),
                new Scalar(0, 0, 0, 0));
        Log.e(TAG, "onCreate: x " +  matchLocation.x);
        Log.e(TAG, "onCreate: y " +  matchLocation.y);
        File file = new File(getCacheDir().getAbsolutePath(), "temp3.png");
        Log.e("zxj1", "onCreate: " + file.getAbsolutePath() );
        Imgcodecs.imwrite(file.getAbsolutePath(), g_src);
        try {
            ivTemp.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将asset文件写入缓存
     */private File copyAssetAndWrite(String fileName) {
         File outFile = null;
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            outFile = new File(cacheDir, fileName);
            if (outFile.exists()){
                outFile.delete();
            }
            outFile.createNewFile();
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }
    @Test
    public void useAppContext() {

    }
}
