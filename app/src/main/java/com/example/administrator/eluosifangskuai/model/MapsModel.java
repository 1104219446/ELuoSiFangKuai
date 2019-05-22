package com.example.administrator.eluosifangskuai.model;

import android.graphics.Canvas;
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
    Paint mappaint=null;
    //画线笔
    Paint linepaint=null;
    //画状态笔
    Paint statepaint=null;
    //游戏区域的高度
    int yHeight;
    //游戏区域宽度
    int xWidth;
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
        linepaint=new Paint();
        linepaint.setColor(Color.BLACK);
        linepaint.setAntiAlias(true);
        mappaint=new Paint();
        mappaint.setAntiAlias(true);
        mappaint.setColor(Color.BLUE);
        statepaint=new Paint();
        statepaint.setAntiAlias(true);
        statepaint.setColor(Color.RED);
        statepaint.setTextSize(100f);
    }
    //绘制地图
    public void drawMaps(Canvas canvas) {
        if(maps!=null) {
            //已存在方块的绘制
            for (int i = 0; i < maps.length; i++) {
                for (int j = 0; j < maps[i].length; j++) {
                    if (maps[i][j]>=0) {
                        mappaint.setColor(Config.COLORID[maps[i][j]]);
                        canvas.drawRect(i * boxSize + 2,
                                j * boxSize + 2,
                                i * boxSize + boxSize - 2,
                                j * boxSize + boxSize - 2,
                                mappaint);
                    }
                }
            }
        }
    }
    //绘制辅助线
    public void drawLines(Canvas canvas) {
        //游戏界面绘制
        for(int i=0;i<maps.length;i++) {
            canvas.drawLine(i*boxSize,0,i*boxSize,
                    yHeight,linepaint);
        }
        for(int i=0;i<maps[0].length;i++) {
            canvas.drawLine(0,i*boxSize,xWidth,
                    i*boxSize,linepaint);
        }
    }
    //绘制暂停或死亡状态
    public void drawState(Canvas canvas,boolean isPause,boolean isover) {
        if(isPause){
            if(!isover) {
                canvas.drawText("暂停了耶QWQ",
                        xWidth / 2 - statepaint.measureText("暂停了耶QWQ") / 2,
                        yHeight / 2, statepaint);
            }
        }
        if(isover) {
            canvas.drawText("游戏结束",
                    xWidth/2 -statepaint.measureText("游戏结束")/2,
                    yHeight/2,statepaint);
        }
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
}
