package com.example.administrator.eluosifangskuai.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.administrator.eluosifangskuai.Config;
import com.example.administrator.eluosifangskuai.model.BoxModel;
import com.example.administrator.eluosifangskuai.model.MapsModel;

import java.util.ArrayList;

public class DrawControl {

    private BoxModel boxModel;

    private MapsModel mapsModel;
    //地图
    private int[][] maps;
    private Paint mapPaint;
    private Paint linePaint;
    private Paint statePaint;
    private int boxSize;
    //游戏过程变量
    private boolean isPause=false;
    private boolean isOver =false;
    private boolean isOpenfz =true;

    public DrawControl(BoxModel boxModel1,MapsModel mapsModel1){
        boxModel=boxModel1;
        mapsModel=mapsModel1;
        if(mapsModel!=null){
            maps=mapsModel.maps;
            mapPaint =mapsModel.getMapPaint();
            linePaint =mapsModel.getLinePaint();
            statePaint =mapsModel.getStatePaint();
            boxSize=mapsModel.boxSize;
        }
    }
    //定位预览方块
    private boolean preMove() {
        if(boxModel.preboxs==null)
            return false;
        //移动成功 不作处理
        if(boxModel.premove(0,1,mapsModel)) {
            return true;
        }
        //移动失败,返回false
        return false;
    }
    //更新预览方块
    public void updatepreBoxs() {
        boxModel.preboxs.clear();
        if(boxModel.boxs!=null) {
            for(int i=0;i<boxModel.boxs.length;i++) {
                Point one=new Point(boxModel.boxs[i].x,boxModel.boxs[i].y);
                boxModel.preboxs.add(one);
            }
            while (preMove()) {
            }
        }
    }
    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        WindowManager wm=(WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }
    //绘制游戏区域
    public void draw(Canvas canvas) {
        //方块绘制
        drawBoxs(canvas);
        //预览方块绘制
        drawpreboxs(canvas);
        if(!isOpenfz) {
            //地图辅助线
            drawLines(canvas);
        }
        //绘制地图
        drawMaps(canvas);
        //绘制状态
        drawState(canvas,isPause, isOver);
    }
    //绘制下一块预览区域
    public void drawNext(Canvas canvas, int width) {
        drawNextBox(canvas,width);
    }

    public BoxModel getBoxModel() {
        return boxModel;
    }

    public MapsModel getMapsModel() {
        return mapsModel;
    }

    public void setBoxModel(BoxModel boxModel) {
        this.boxModel = boxModel;
    }

    public void setMapsModel(MapsModel mapsModel) {
        this.mapsModel = mapsModel;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        this.isOver = over;
    }

    public boolean isOpenfz() {
        return isOpenfz;
    }

    public void setOpenfz(boolean openfz) {
        this.isOpenfz = openfz;
    }
    //绘制地图
    private void drawMaps(Canvas canvas) {
        if(maps!=null) {
            //已存在方块的绘制
            for (int i = 0; i < maps.length; i++) {
                for (int j = 0; j < maps[i].length; j++) {
                    if (maps[i][j]>=0) {
                        mapPaint.setColor(Config.COLORID[maps[i][j]]);
                        canvas.drawRect(i * boxSize + 2,
                                j * boxSize + 2,
                                i * boxSize + boxSize - 2,
                                j * boxSize + boxSize - 2,
                                mapPaint);
                    }
                }
            }
        }
    }
    //绘制辅助线
    private void drawLines(Canvas canvas) {
        //游戏界面绘制
        for(int i=0;i<maps.length;i++) {
            canvas.drawLine(i*boxSize,0,i*boxSize,
                    mapsModel.getyHeight(), linePaint);
        }
        for(int i=0;i<maps[0].length;i++) {
            canvas.drawLine(0,i*boxSize,mapsModel.getxWidth(),
                    i*boxSize, linePaint);
        }
    }
    //绘制暂停或死亡状态
    private void drawState(Canvas canvas,boolean isPause,boolean isover) {
        if(isPause){
            if(!isover) {
                canvas.drawText("暂停了耶QWQ",
                        mapsModel.getxWidth() / 2 - statePaint.measureText("暂停了耶QWQ") / 2,
                        mapsModel.getyHeight() / 2, statePaint);
            }
        }
        if(isover) {
            canvas.drawText("游戏结束",
                    mapsModel.getxWidth()/2 - statePaint.measureText("游戏结束")/2,
                    mapsModel.getyHeight()/2, statePaint);
        }
    }
    //画方块
    private void drawBoxs(Canvas canvas) {
        Point[]boxs=boxModel.boxs;
        if(boxs!=null) {
            boxModel.boxpaint.setColor(Config.COLORID[boxModel.boxtype]);
            //方块绘制
            for (int i = 0; i < boxs.length; i++) {
                canvas.drawRect(boxs[i].x * boxSize + 2,
                        boxs[i].y * boxSize + 2,
                        boxs[i].x * boxSize + boxSize - 2,
                        boxs[i].y * boxSize + boxSize - 2,
                        boxModel.boxpaint);
            }
        }
    }
    //画下一块方块
    private void drawNextBox(Canvas canvas, int width) {
        int boxNextSize=boxModel.boxNextSize;
        if(boxNextSize==0){
            boxNextSize=width/5;
        }
        if(boxModel.nextProduceBox !=null) {
            for(int i = 0; i< boxModel.nextProduceBox.getBoxTypeStrategy().getBoxT().length; i++)
            {
                int xx= boxModel.nextProduceBox.getBoxTypeStrategy().getBoxT()[i].x;
                int yy= boxModel.nextProduceBox.getBoxTypeStrategy().getBoxT()[i].y;
                xx-=4;
                switch (boxModel.boxNextType){
                    //田
                    case 0:xx+=1;yy+=1;break;
                    //L
                    case 1:xx+=1;yy+=1;break;
                    //反L
                    case 2:xx+=1;yy+=1;break;
                    //S
                    case 3:yy+=1;break;
                    //Z
                    case 4:yy+=1;break;
                    //I
                    case 5:yy+=2;break;
                    //T
                    case 6:yy+=1;break;
                    //两点
                    case 7:xx+=1;yy+=2;break;
                    //一点
                    case 8:xx+=1;yy+=2;break;
                    default:break;
                }
                boxModel.nextpaint.setColor(Config.COLORID[
                        boxModel.nextProduceBox.getBoxTypeStrategy().getBoxType()]);
                boxModel.nextpaint.setAlpha(128);
                canvas.drawRect((xx)*boxNextSize + 2,
                        yy*boxNextSize + 2,
                        (xx+1)*boxNextSize - 2,
                        (yy+1)*boxNextSize - 2,
                        boxModel.nextpaint
                );
            }
        }
    }
    //画预览方块
    private void drawpreboxs(Canvas canvas){
        boxModel.preboxpaint.setColor(boxModel.boxpaint.getColor());
        boxModel.preboxpaint.setAlpha(25);
        ArrayList<Point>preboxs=boxModel.preboxs;
        if(preboxs!=null) {
            //方块绘制
            for (int i = 0; i < preboxs.size(); i++) {
                canvas.drawRect(preboxs.get(i).x * boxSize + 2,
                        preboxs.get(i).y * boxSize + 2,
                        preboxs.get(i).x * boxSize + boxSize - 2,
                        preboxs.get(i).y * boxSize + boxSize - 2,
                        boxModel.preboxpaint);
            }
        }
    }

}
