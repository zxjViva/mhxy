package com.zxj.mhxyopencv.utils.file;

import java.io.Closeable;
import java.io.IOException;

public class FileUtils {
    public static void closeStream(Closeable stream){
        if (stream != null){
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
