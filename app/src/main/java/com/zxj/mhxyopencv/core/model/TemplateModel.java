package com.zxj.mhxyopencv.core.model;

public class TemplateModel {
    public CoordModel coordModel;
    public boolean isMatch;

    public TemplateModel(CoordModel coordModel, boolean isMatch) {
        this.coordModel = coordModel;
        this.isMatch = isMatch;
    }
}
