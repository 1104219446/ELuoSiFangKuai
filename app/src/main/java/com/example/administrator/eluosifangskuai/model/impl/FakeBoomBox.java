package com.example.administrator.eluosifangskuai.model.impl;

import android.graphics.Point;

import com.example.administrator.eluosifangskuai.model.base.BaseBox;
import com.example.administrator.eluosifangskuai.model.inf.BoxTypeStrategy;

public class FakeBoomBox extends BaseBox implements BoxTypeStrategy {

    @Override
    public Point[] getBoxT() {
        return new Point[]{new Point(4,1),new Point(6,0),new Point(3,0)
                ,new Point(5,1),new Point(5,2),new Point(4,2)};
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }

}
