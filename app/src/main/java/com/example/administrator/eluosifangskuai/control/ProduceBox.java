package com.example.administrator.eluosifangskuai.control;

import android.graphics.Paint;
import android.graphics.Point;

import com.example.administrator.eluosifangskuai.model.inf.BoxTypeStrategy;


//策略模式用于生产各种方块
public class ProduceBox {
    private BoxTypeStrategy boxTypeStrategy;

    public BoxTypeStrategy getBoxTypeStrategy() {
        return boxTypeStrategy;
    }

    public void setBoxTypeStrategy(BoxTypeStrategy boxTypeStrategy) {
        this.boxTypeStrategy = boxTypeStrategy;
    }

}
