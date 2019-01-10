package com.zxj.mhxyopencv;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.zxj.mhxyopencv.utils.file.CmdUtils;
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
        CmdUtils.runCMD("chmod 777 " + InstrumentationRegistry.getContext().getCacheDir().getAbsolutePath(),true,true);
        Log.e("zxj1", "getContext: " + InstrumentationRegistry.getContext().getPackageName());
        Log.e("zxj1", "getTargetContext: " + InstrumentationRegistry.getTargetContext().getPackageName());
//        File targetFile = getTargetFile(InstrumentationRegistry.getTargetContext(), "dyt1.png", new File(InstrumentationRegistry.getTargetContext().getCacheDir(),"dyt1.png").getAbsolutePath());
//        Log.e("zxj1", "useAppContext: " + targetFile.getAbsolutePath());
//        Mat imread = Imgcodecs.imread(targetFile.getAbsolutePath());
//        int rows = imread.rows();
//        int cols = imread.cols();
//        for (int i = 0; i < rows; i++) {
//            for (int i1 = 0; i1 < cols; i1++) {
//                double[] doubles = imread.get(i, i1);
//                double r = doubles[0];
//                double g = doubles[1];
//                double b = doubles[2];
//                Log.e("zxj1", "useAppContext: " + r);
//                Log.e("zxj1", "useAppContext: " + g);
//                Log.e("zxj1", "useAppContext: " + b);
//            }
//        }

    }

    //    private void findAndClick(UiDevice instance, Context appContext) {
//        File targetFile = getTargetFile(appContext, "key_startgame.png", "key_startgame.png");
//        File file = screenCat(appContext,instance);
//        double[] template = template(appContext, targetFile, file);
//        int x = (int) template[0] * 2;
//        int y = (int) template[1] * 2;
//        Log.e("zxj", "x :" + x );
//        Log.e("zxj", "y :" + y );
//        instance.click(x,y);
//    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public File screenCat(Context context, UiDevice device) {
        File file = new File(context.getCacheDir(), System.currentTimeMillis() + ".png");
        device.takeScreenshot(file);
        return file;
    }

    public File getTargetFile(Context context, String filename, String savePath) {
        return CopyFile.copyFileFromAssets(context, filename, savePath);
    }

    public double[] template(Context context, File targetFile, File srcFile) {
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
        Log.e("zxj1", "onCreate: " + file.getAbsolutePath());
        Imgcodecs.imwrite(file.getAbsolutePath(), g_src);
        double[] coord = new double[]{(matchLocation.x + g_tem.cols()) / 2, (matchLocation.y + g_tem.rows()) / 2};

        return coord;
    }
}
