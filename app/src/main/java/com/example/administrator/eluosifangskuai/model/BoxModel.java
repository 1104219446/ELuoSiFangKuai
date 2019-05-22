package com.example.administrator.eluosifangskuai.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.administrator.eluosifangskuai.Config;
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
    public Point[] boxs=null;
    //类型
    public int boxtype=0;
    //方块大小
    public int boxSize;
    //方块画笔
    public Paint boxpaint=null;
    //预览方块画笔
    public Paint preboxpaint=null;
    //下一块方块画笔
    public Paint nextpaint=null;
    //下一块方块
    public ProduceBox nextProduceBox;
    //下一块方块类型
    public int boxNextType;
    //下一块方块格子大小
    public int boxNextSize;
    //随机数
    private Random random=new Random();
    //预览方块
    public ArrayList<Point> preboxs=null;
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
            nextProduceBox =new ProduceBox();
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
        //生成下一块方块
        boxNextType=random.nextInt(8);

        //设置画笔颜色
        boxpaint.setColor(Config.COLORID[boxNextType]);
        switch (boxNextType) {
            //田
            case 0:
                nextProduceBox.setBoxTypeStrategy(new TianBox());
                break;
            //L
            case 1:
                nextProduceBox.setBoxTypeStrategy(new LBox());
                break;
            //反L
            case 2:
                nextProduceBox.setBoxTypeStrategy(new ReverseLBox());
                break;
            //S
            case 3:
                nextProduceBox.setBoxTypeStrategy(new SBox());
                break;
            //Z
            case 4:
                nextProduceBox.setBoxTypeStrategy(new ZBox());
                break;
            //I
            case 5:
                nextProduceBox.setBoxTypeStrategy(new IBox());
                break;
            //T
            case 6:
                nextProduceBox.setBoxTypeStrategy(new TBox());
                break;
            //两点
            case 7:
                nextProduceBox.setBoxTypeStrategy(new TwoPointBox());
                break;
            //假炸弹
            case 8:
                nextProduceBox.setBoxTypeStrategy(new FakeBoomBox());
                break;
        }
        //设置下一块方块类型
        nextProduceBox.getBoxTypeStrategy().setBoxType(boxNextType);

    }
    //画方块
    public void drawBoxs(Canvas canvas) {
        if(boxs!=null) {
            boxpaint.setColor(Config.COLORID[boxtype]);
            //方块绘制
            for (int i = 0; i < boxs.length; i++) {
                canvas.drawRect(boxs[i].x * boxSize + 2,
                        boxs[i].y * boxSize + 2,
                        boxs[i].x * boxSize + boxSize - 2,
                        boxs[i].y * boxSize + boxSize - 2,
                        boxpaint);
            }
        }
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
    //画下一块方块
    public void drawNext(Canvas canvas, int width) {
        if(boxNextSize==0){
            boxNextSize=width/5;
        }
        if(nextProduceBox !=null) {
            for(int i = 0; i< nextProduceBox.getBoxTypeStrategy().getBoxT().length; i++)
            {
                int xx= nextProduceBox.getBoxTypeStrategy().getBoxT()[i].x;
                int yy= nextProduceBox.getBoxTypeStrategy().getBoxT()[i].y;
                xx-=3;
                switch (boxNextType){
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
                nextpaint.setColor(Config.COLORID[boxNextType]);
                nextpaint.setAlpha(128);
                canvas.drawRect((xx)*boxNextSize + 2,
                        yy*boxNextSize + 2,
                        (xx+1)*boxNextSize - 2,
                        (yy+1)*boxNextSize - 2,
                        nextpaint
                        );
            }
        }
    }
    //画预览方块
    public void drawpreboxs(Canvas canvas){
        preboxpaint.setColor(boxpaint.getColor());
        preboxpaint.setAlpha(25);
        if(preboxs!=null) {
            //方块绘制
            for (int i = 0; i < preboxs.size(); i++) {
                canvas.drawRect(preboxs.get(i).x * boxSize + 2,
                        preboxs.get(i).y * boxSize + 2,
                        preboxs.get(i).x * boxSize + boxSize - 2,
                        preboxs.get(i).y * boxSize + boxSize - 2,
                        preboxpaint);
            }
        }
    }

}
