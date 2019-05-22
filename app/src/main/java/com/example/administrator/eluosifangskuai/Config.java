package com.example.administrator.eluosifangskuai;

import android.content.SharedPreferences;
import android.graphics.Color;

import java.security.PublicKey;

/**
 * Created by ZZK on 2018/2/1.
 */

public class Config {
    //地图宽X方向格子数
    public static final int MAPX=10;
    //地图高Y方向格子数
    public static final int MAPY=MAPX*2;
    //地图x方向宽度
    public static int XWHITH;
    //地图y方向宽度
    public static int YHEIGHT;
    //间距
    public static int PADDING;
    //单位间距分母
    public static final int SPLIT_PADDING=25;
    //本地储存数据用
    public static SharedPreferences sp=null;
    //本地储存编辑器
    public static SharedPreferences.Editor editor=null;
    //方块颜色数组
    public static final int[] COLORID={
            Color.rgb(165,42,42),
            Color.rgb(220,20,60),
            Color.GREEN,
            Color.rgb(135,206,235),
            Color.BLUE,
            Color.rgb(255,215,0),
            Color.rgb(127,255,212),
            Color.rgb(255,0,255),
            Color.BLACK,
            Color.rgb(0,206,209)
    };

}
