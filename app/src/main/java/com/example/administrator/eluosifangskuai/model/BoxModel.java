package com.example.administrator.eluosifangskuai.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.example.administrator.eluosifangskuai.Config;
import com.example.administrator.eluosifangskuai.control.BoxFactory;
import com.example.administrator.eluosifangskuai.control.ProduceBox;
import com.example.administrator.eluosifangskuai.model.impl.FakeBoomBox;
import com.example.administrator.eluosifangskuai.model.impl.IBox;
import com.example.administrator.eluosifangskuai.model.impl.LBox;
import com.example.administrator.eluosifangskuai.model.impl.ReverseLBox;
import com.example.administrator.eluosifangskuai.model.impl.SBox;
import com.example.administrator.eluosifangskuai.model.impl.TBox;
import com.example.administrator.eluosifangskuai.model.impl.TianBox;
import com.example.administrator.eluosifangskuai.model.impl.TwoPointBox;
import com.example.administrator.eluosifangskuai.model.impl.ZBox;

import java.util.ArrayList;
import java.util.Random;

/**
 * 方块模型
 * Created by ZZK on 2018/2/1.
 */

public class BoxModel {
    //方块
    public Point[] boxs;
    //类型
    public int boxtype;
    //方块大小
    public int boxSize;
    //方块画笔
    public Paint boxpaint;
    //预览方块画笔
    public Paint preboxpaint;
    //下一块方块画笔
    public Paint nextpaint;
    //下一块方块
    public ProduceBox nextProduceBox;
    //下一块方块类型
    public int boxNextType;
    //下一块方块格子大小
    public int boxNextSize;
    //预览方块
    public ArrayList<Point> preboxs;

    public BoxModel(int boxsize) {
        this.boxSize=boxsize;
        boxpaint=new Paint();
        boxpaint.setAntiAlias(true);
        boxpaint.setColor(Color.BLACK);
        preboxpaint=new Paint();
        preboxpaint.setAntiAlias(true);
        preboxpaint.setColor(Color.BLACK);
        nextpaint=new Paint();
        nextpaint.setAntiAlias(true);
        nextpaint.setColor(Color.BLACK);
        nextpaint.setAlpha(100);
        preboxs= new ArrayList<>();
    }
    //生成新方块
    public void newboxs() {
        if(nextProduceBox ==null){
            newBoxsNext();
        }
        //赋值当前方块
        boxs= nextProduceBox.getBoxTypeStrategy().getBoxT();
        boxtype= nextProduceBox.getBoxTypeStrategy().getBoxType();
        for(int i=0;i<boxs.length;i++) {
            Point one=new Point(boxs[i].x,boxs[i].y);
            preboxs.add(one);
        }
        //生成下一块
        newBoxsNext();
    }
    //生成下一块方块
    private void newBoxsNext() {
        nextProduceBox =BoxFactory.getRandomNextBox();
        Log.d("dd", "newBoxsNext: "+nextProduceBox.getBoxTypeStrategy().getBoxType());
        //设置画笔颜色
        boxpaint.setColor(Config.COLORID[nextProduceBox.getBoxTypeStrategy().getBoxType()]);

    }
    //移动当前方块
    public boolean move(int x, int y,MapsModel mapsModel) {
        if(boxs==null)
            return false;
        for(int i=0;i<boxs.length;i++) {
            int xx=boxs[i].x + x;
            int yy=boxs[i].y + y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
        }
        for(int i=0;i<boxs.length;i++) {
            int xx=boxs[i].x + x;
            int yy=boxs[i].y + y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
            boxs[i].x=xx;
            boxs[i].y=yy;
        }
        return true;
    }
    //移动预览方块
    public boolean premove(int x, int y,MapsModel mapsModel) {
        if(preboxs==null)
            return false;
        for(int i=0;i<preboxs.size();i++) {
            int xx=preboxs.get(i).x + x;
            int yy=preboxs.get(i).y + y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
        }
        for(int i=0;i<preboxs.size();i++) {
            int xx=preboxs.get(i).x + x;
            int yy=preboxs.get(i).y + y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
            preboxs.get(i).x=xx;
            preboxs.get(i).y=yy;
        }
        return true;
    }
    //出界判断  返回true越界
    private boolean checkBoundary(int x,int y,MapsModel mapsModel) {
        return (x<0||y<0||x>=mapsModel.maps.length||
                y>=mapsModel.maps[0].length||mapsModel.maps[x][y]>=0);
    }
    //方块旋转
    public boolean rotate(MapsModel mapsModel) {
        //如果是田字形
        if(boxtype==0){
            return false;
        }
        for(int i=0;i<boxs.length;i++) {
            int xx=-boxs[i].y+boxs[0].y+boxs[0].x;
            int yy=boxs[i].x-boxs[0].x+boxs[0].y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
        }
        for(int i=0;i<boxs.length;i++) {
            int xx=-boxs[i].y+boxs[0].y+boxs[0].x;
            int yy=boxs[i].x-boxs[0].x+boxs[0].y;
            if(checkBoundary(xx,yy,mapsModel)) {
                return false;
            }
            boxs[i].x=xx;
            boxs[i].y=yy;
        }
        return true;
    }
}
