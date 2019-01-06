package org.opencv;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class main {
    public static void main(String[] args) {
//        System.loadLibrary("libopencv_java4");
        OpenCVLoader.initDebug();
        Mat g_tem = Imgcodecs.imread("C:\\Users\\zxj\\Desktop\\pic\\greenDot.png");
        Mat g_src = Imgcodecs.imread("C:\\Users\\zxj\\Desktop\\pic\\map.png");
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

        Imgcodecs.imwrite("C:\\Users\\zxj\\Desktop\\pic\\match.jpg", g_src);
    }
}
