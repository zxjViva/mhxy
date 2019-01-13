package com.zxj.mhxyopencv.core.model;

import org.opencv.core.Point;

public class CoordModel extends Point {
    public int x;
    public int y;

    public CoordModel(int x, int y) {
        this.x = x;
        this.y = y;
        super.x = x;
        super.y = y;
    }
}
