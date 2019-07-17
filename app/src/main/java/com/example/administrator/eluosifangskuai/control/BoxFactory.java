package com.example.administrator.eluosifangskuai.control;

import android.util.Log;

import com.example.administrator.eluosifangskuai.model.impl.FakeBoomBox;
import com.example.administrator.eluosifangskuai.model.impl.IBox;
import com.example.administrator.eluosifangskuai.model.impl.LBox;
import com.example.administrator.eluosifangskuai.model.impl.ReverseLBox;
import com.example.administrator.eluosifangskuai.model.impl.SBox;
import com.example.administrator.eluosifangskuai.model.impl.TBox;
import com.example.administrator.eluosifangskuai.model.impl.TianBox;
import com.example.administrator.eluosifangskuai.model.impl.TwoPointBox;
import com.example.administrator.eluosifangskuai.model.impl.ZBox;
import com.example.administrator.eluosifangskuai.single.ProduceBoxSingle;

import java.util.Random;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/5/28
 */
public class BoxFactory {

    private static Random random=new Random();
    //随机获得一个方块
    public static ProduceBox getRandomNextBox(){
        //此处使用单例模式避免重复new
        ProduceBox nextProduceBox=ProduceBoxSingle.getInstance();
        //生成下一块方块此处降低难度暂时不要假炸弹
        //int boxNextType=getRandomBalance();
        int boxNextType=getRandomBalance();
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
            //炸弹
            case 8:
                nextProduceBox.setBoxTypeStrategy(new FakeBoomBox());
                break;
        }
        //设置下一块方块类型
        Log.d("dd", "getRandomNextBox: "+boxNextType);
        nextProduceBox.getBoxTypeStrategy().setBoxType(boxNextType);
        return nextProduceBox;
    }

    public static int getRandomBalance(){
        int ans=random.nextInt(100);
        //炸弹出现几率为1/20
        if(ans<5){
            return 8;
        }else{
            return random.nextInt(8);
        }
    }

}
