package com.example.administrator.eluosifangskuai.model;

import android.graphics.Color;
import android.graphics.Paint;

import com.example.administrator.eluosifangskuai.Config;

import java.util.Random;

/**
 * Created by ZZK on 2018/2/1.
 */

public class MapsModel {
    //地图
    public int[][] maps;
    //方块大小
    public int boxSize;
    //地图画笔
    private Paint mapPaint;
    //画线笔
    private Paint linePaint;
    //画状态笔
    private Paint statePaint;
    //游戏区域的高度
    private int yHeight;
    //游戏区域宽度
    private int xWidth;
    //初始化地图模型
    public MapsModel(int yHeight, int xWidth, int boxsize) {
        this.yHeight=yHeight;
        this.xWidth=xWidth;
        this.boxSize=boxsize;
        maps=new int[Config.MAPX][Config.MAPY];
        cleanMaps();
        initallpaint();
    }
    //实例化画笔
    private void initallpaint() {
        linePaint =new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);
        mapPaint =new Paint();
        mapPaint.setAntiAlias(true);
        mapPaint.setColor(Color.BLUE);
        statePaint =new Paint();
        statePaint.setAntiAlias(true);
        statePaint.setColor(Color.RED);
        statePaint.setTextSize(100f);
    }

    //清除地图
    public void cleanMaps() {
        for(int x=0;x<maps.length;x++)
        {
            for(int y=0;y<maps[0].length;y++)
            {
                maps[x][y]=-1;
            }
        }
    }
    //消行判断
    public int cleanLine() {
        int lines=0;
        for(int y=0;y<maps[0].length;y++)
        {
            if(checkline(y))
            {
                deleteline(y);
                lines++;
            }
        }
        return lines;
    }
    //清除一行
    private void deleteline(int dy) {
        for(int y=dy;y>=0;y--){
            if(y!=0) {
                for (int x = 0; x < maps.length; x++) {
                    maps[x][y] = maps[x][y - 1];
                }
            }else{
                for (int x = 0; x < maps.length; x++) {
                    maps[x][y] = -1;
                }
            }

        }
    }
    //检查一行
    private boolean checkline(int y) {
        for(int x=0;x<maps.length;x++) {
            if(maps[x][y]<0) {
                return false;
            }
        }
        return true;
    }
    //底部增加一行
    public void addLines(){
        for(int i=0;i<maps[0].length;i++){
            if(i!=(maps[0].length-1)) {
                for (int j = 0; j < maps.length; j++) {
                    maps[j][i]=maps[j][i+1];
                }
            }else{
                Random random=new Random();
                for(int j=0;j<maps.length;j++) {
                    if (random.nextBoolean()) {
                        maps[j][i]=9;
                    }else{
                        maps[j][i]=-1;
                    }
                }
            }
        }
    }

    public Paint getMapPaint() {
        return mapPaint;
    }

    public void setMapPaint(Paint mapPaint) {
        this.mapPaint = mapPaint;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public Paint getStatePaint() {
        return statePaint;
    }

    public void setStatePaint(Paint statePaint) {
        this.statePaint = statePaint;
    }

    public int getyHeight() {
        return yHeight;
    }

    public void setyHeight(int yHeight) {
        this.yHeight = yHeight;
    }

    public int getxWidth() {
        return xWidth;
    }

    public void setxWidth(int xWidth) {
        this.xWidth = xWidth;
    }
}
