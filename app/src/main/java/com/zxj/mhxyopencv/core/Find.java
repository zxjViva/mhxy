package com.zxj.mhxyopencv.core;

import android.util.Log;

import com.zxj.mhxyopencv.core.model.Character;
import com.zxj.mhxyopencv.core.model.CoordModel;
import com.zxj.mhxyopencv.core.model.Monster;
import com.zxj.mhxyopencv.core.model.TemplateModel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Find {
    private final static String TAG = "Find";
    private static int[] monsterNameColorRGB = new int[]{235,215,52};
    private static int monsterNameColorOffset = 5;
    public static List<Monster> findMonsters(Mat mat) {
        List<Monster> monsters = new ArrayList<>();
        int rows = mat.rows();
        int cols = mat.cols();
        Log.e(TAG, "findMonsters: rows" + rows );
        Log.e(TAG, "findMonsters: cols" + cols );
        Mat mat1 = new Mat(rows, cols, CvType.CV_32FC3);
        for (int i = 0; i < rows; i++) {
            for (int i1 = 0; i1 < cols; i1++) {
                double[] doubles = mat.get(i, i1);
                int b = (int) doubles[0];
                int g = (int) doubles[1];
                int r = (int) doubles[2];
                if (colorOffset(r, monsterNameColorRGB[0], monsterNameColorOffset)
                        && colorOffset(g, monsterNameColorRGB[1], monsterNameColorOffset)
                        && colorOffset(b, monsterNameColorRGB[2], monsterNameColorOffset)) {
                    mat1.put(i,i1,doubles);
                    Monster monster = new Monster();
                    monster.relativeCoord = new CoordModel(i1,i);
                    monsters.add(monster);
                }
            }
        }
//        Point matchLocation = new Point();
//        Imgproc.rectangle(mat1, matchLocation,
//                new Point(16 + 50,938 + 50),
//                new Scalar(0, 0, 0, 0));
//        Imgcodecs.imwrite(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), System.currentTimeMillis()+".png").getAbsolutePath(),mat1);
        Log.e(TAG, "findMonsters size : " + monsters.size() );
        return monsters;
    }
    public static Character findCharacter(Mat mat) {
        Character character = new Character();
        int rows = mat.rows();
        int cols = mat.cols();
        character.relativeCoord = new CoordModel(cols/2,rows/2);
        Log.e(TAG, "findCharacter: (" + character.relativeCoord.x + "," + character.relativeCoord.y+")" );
        return character;
    }
    public static CoordModel findNearsetDistance(final Character character, List<Monster> monsters){
        Collections.sort(monsters, new Comparator<Monster>() {
            @Override
            public int compare(Monster o1, Monster o2) {
                CoordModel relativeCoord1 = o1.relativeCoord;
                int o1Distance = (int) (Math.pow(relativeCoord1.x -  character.relativeCoord.x,2)
                        + Math.pow(relativeCoord1.y -  character.relativeCoord.y,2));
                CoordModel relativeCoord2 = o2.relativeCoord;
                int o2Distance = (int) (Math.pow(relativeCoord2.x -  character.relativeCoord.x,2)
                        + Math.pow(relativeCoord2.y -  character.relativeCoord.y,2));
                return o1Distance - o2Distance;
            }
        });
        for (Monster monster : monsters) {
            Log.e(TAG, "findNearsetDistance: (" + monster.relativeCoord.x + "," + monster.relativeCoord.y+")" );
        }
        return monsters.size() > 0 ? monsters.get(0).relativeCoord : null;
    }
    private static boolean colorOffset(int color, int targetColor, int offset) {
        if (color > (targetColor - offset) && color < (targetColor + offset)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isFighting(Mat mat){
        Rect roi = new Rect(50,0,mat.cols()/2,mat.rows()/7);
        //指定图像格式大小
        mat=new Mat(mat,roi);
        Mat imFighting = Imgcodecs.imread(ResFilePathConstants.getKeyFightingPath());
        Core.MinMaxLocResult minMaxLocResult = matchTemplate(mat, imFighting);
        return minMaxLocResult.minVal > 0.0002;
    }
    public static TemplateModel isAutoFight(Mat mat){
        Rect roi = new Rect(mat.cols()/10*9,mat.rows()/7*6,mat.cols()/10,mat.rows()/7);
        //指定图像格式大小
        Mat newMat=new Mat(mat,roi);
        Mat imFighting = Imgcodecs.imread(ResFilePathConstants.getKeyAutoFightPath());
        Core.MinMaxLocResult minMaxLocResult = matchTemplate(newMat, imFighting);
        Log.e(TAG, "isAutoFight x: " + minMaxLocResult.minLoc.x );
        Log.e(TAG, "isAutoFight y: " + minMaxLocResult.minLoc.y );
        CoordModel coordModel = new CoordModel((int)(minMaxLocResult.minLoc.x + (mat.cols()/10*9) +imFighting.cols()/2),
                (int)(minMaxLocResult.minLoc.y+(mat.rows()/7*6)+imFighting.rows()/2));
        TemplateModel templateModel = new TemplateModel(coordModel,minMaxLocResult.minVal < 0.0002);
        return templateModel;
    }

    public static Core.MinMaxLocResult matchTemplate(Mat srcMat,Mat findMat){
        int result_rows = srcMat.rows() - findMat.rows() + 1;
        int result_cols = srcMat.cols() - findMat.cols() + 1;
        Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(srcMat, findMat, g_result, Imgproc.TM_SQDIFF);
        Core.normalize(g_result, g_result);
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);
        Point matchLocation = mmlr.minLoc;
        Imgproc.rectangle(srcMat, matchLocation,
                new Point(matchLocation.x + findMat.cols(), matchLocation.y + findMat.rows()),
                new Scalar(0, 255, 0));
        File file = new File(ResFilePathConstants.tempResFilePath, "find_temp" + System.currentTimeMillis() + ".png");
        Imgcodecs.imwrite(file.getAbsolutePath(), srcMat);
        Log.e(TAG, "matchTemplate mmlr.minVal: " + mmlr.minVal );
        Log.e(TAG, "matchTemplate mmlr.maxVal: " + mmlr.maxVal );
        return mmlr;
    }
}
