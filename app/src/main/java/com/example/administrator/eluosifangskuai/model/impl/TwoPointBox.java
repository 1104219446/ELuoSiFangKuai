package com.example.administrator.eluosifangskuai.model.impl;

import android.graphics.Point;

import com.example.administrator.eluosifangskuai.model.base.BaseBox;
import com.example.administrator.eluosifangskuai.model.inf.BoxTypeStrategy;

public class TwoPointBox extends BaseBox implements BoxTypeStrategy {
    @Override
    public Point[] getBoxT() {
        return new Point[]{new Point(5,0),new Point(4,0),};
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }
}
