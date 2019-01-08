package com.zxj.mhxyopencv;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.zxj.mhxyopencv.utils.file.CopyFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        UiDevice instance = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.e("zxj", "useAppContext: " );
        File targetFile = getTargetFile(appContext, "key_startgame.png", "key_startgame.png");
        File file = screenCat(appContext,instance);
        double[] template = template(appContext, targetFile, file);
        int x = (int) template[0] * 2;
        int y = (int) template[1] * 2;
        Log.e("zxj", "x :" + x );
        Log.e("zxj", "y :" + y );
        instance.click(x,y);
    }
    public File screenCat(Context context,UiDevice device){
        File file = new File(context.getCacheDir(), System.currentTimeMillis() + ".png");
        device.takeScreenshot(file);
        return file;
    }
    public File getTargetFile(Context context,String filename,String saveName){
       return CopyFile.copyFileFromAssets(context,filename,new File(context.getCacheDir(),saveName).getAbsolutePath());
    }
    public double[] template(Context context,File targetFile,File srcFile){
        OpenCVLoader.initDebug();
        Mat g_tem = Imgcodecs.imread(targetFile.getAbsolutePath());
        Mat g_src = Imgcodecs.imread(srcFile.getAbsolutePath());
        // Example of a call to a native method
        int result_rows = g_src.rows() - g_tem.rows() + 1;
        int result_cols = g_src.cols() - g_tem.cols() + 1;
        Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(g_src, g_tem, g_result, Imgproc.TM_CCORR_NORMED);
        Core.normalize(g_result, g_result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Point matchLocation = new Point();
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);
        matchLocation = mmlr.maxLoc; // 此处使用maxLoc还是minLoc取决于使用的匹配算法
        Imgproc.rectangle(g_src, matchLocation,
                new Point(matchLocation.x + g_tem.cols(), matchLocation.y + g_tem.rows()),
                new Scalar(0, 0, 0, 0));
        File file = new File(context.getCacheDir().getAbsolutePath(), "temp.png");
        Log.e("zxj1", "onCreate: " + file.getAbsolutePath() );
        Imgcodecs.imwrite(file.getAbsolutePath(), g_src);
        double[] coord = new double[]{(matchLocation.x + g_tem.cols())/2,(matchLocation.y + g_tem.rows())/2};

        return coord;
    }
}
