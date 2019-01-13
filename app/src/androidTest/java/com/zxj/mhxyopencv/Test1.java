package com.zxj.mhxyopencv;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.zxj.mhxyopencv.core.Action;
import com.zxj.mhxyopencv.core.Find;
import com.zxj.mhxyopencv.core.ResFilePathConstants;
import com.zxj.mhxyopencv.core.model.Character;
import com.zxj.mhxyopencv.core.model.CoordModel;
import com.zxj.mhxyopencv.core.model.Monster;
import com.zxj.mhxyopencv.core.model.TemplateModel;
import com.zxj.mhxyopencv.utils.file.CopyFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class Test1 {
    private final String TAG = "Test1";
    @Test
    public void case1() throws InterruptedException {
        Log.e(TAG, "start" );
        OpenCVLoader.initDebug();
        CopyFile.copyFilesFromAssets(InstrumentationRegistry.getTargetContext(),"res",ResFilePathConstants.resFilePath);
        while (true){
            File currentScrennFile = Action.screenCut();
            Mat imread = Imgcodecs.imread(currentScrennFile.getAbsolutePath());
            boolean fighting = Find.isFighting(imread);
            if (fighting){
                TemplateModel autoFightModel = Find.isAutoFight(imread);
                if (autoFightModel.isMatch){
                    Action.click(autoFightModel.coordModel);
                }else {

                }
            }else {
                List<Monster> monsters = Find.findMonsters(imread);
                Character character = Find.findCharacter(imread);
                CoordModel nearsetDistance = Find.findNearsetDistance(character, monsters);
                ArrayList<CoordModel> points = new ArrayList<>();
                points.add(nearsetDistance);
                points.add(new CoordModel(nearsetDistance.x + 20,nearsetDistance.y +20));
                points.add(new CoordModel(nearsetDistance.x - 20,nearsetDistance.y -20));
                Action.click(points);
            }
            File file = new File(ResFilePathConstants.tempResFilePath);
            if (file.getTotalSpace() > 1024*1024*1024*500){
                for (File file1 : file.listFiles()) {
                    file1.delete();
                }
            }
            Thread.sleep(5000);
        }
    }
    public File getTargetFile(Context context, String filename, String savePath) {
        return CopyFile.copyFileFromAssets(context, filename, savePath);
    }

    public boolean colorOffset(int color,int targetColor,int offset){
        if (color > (targetColor-offset) && color < (targetColor+offset)){
            return true;
        }else {
            return false;
        }
    }
}
