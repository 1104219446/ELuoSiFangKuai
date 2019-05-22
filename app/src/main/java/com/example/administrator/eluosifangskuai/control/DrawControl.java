package com.example.administrator.eluosifangskuai.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.administrator.eluosifangskuai.model.BoxModel;
import com.example.administrator.eluosifangskuai.model.MapsModel;

public class DrawControl {

    private BoxModel boxModel;
    private MapsModel mapsModel;
    //游戏过程变量
    private boolean isPause=false;
    private boolean isover=false;
    private boolean  isopenfz=true;

    public DrawControl(BoxModel boxModel1,MapsModel mapsModel1){
        boxModel=boxModel1;
        mapsModel=mapsModel1;
    }

    //定位预览方块
    public boolean preMove() {
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
        boxModel.drawBoxs(canvas);
        //预览方块绘制
        boxModel.drawpreboxs(canvas);
        if(!isopenfz) {
            //地图辅助线
            mapsModel.drawLines(canvas);
        }
        //绘制地图
        mapsModel.drawMaps(canvas);
        //绘制状态
        mapsModel.drawState(canvas,isPause,isover);
    }
    //绘制下一块预览区域
    public void drawNext(Canvas canvas, int width) {
        boxModel.drawNext(canvas,width);
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

    public boolean isIsover() {
        return isover;
    }

    public void setIsover(boolean isover) {
        this.isover = isover;
    }

    public boolean isIsopenfz() {
        return isopenfz;
    }

    public void setIsopenfz(boolean isopenfz) {
        this.isopenfz = isopenfz;
    }
}
