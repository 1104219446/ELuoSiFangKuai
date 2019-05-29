package com.example.administrator.eluosifangskuai.single;

import com.example.administrator.eluosifangskuai.control.ProduceBox;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/5/28
 */
public class ProduceBoxSingle {
    private static volatile ProduceBox instance;
    private ProduceBoxSingle(){}

    public static ProduceBox getInstance(){
        if(instance==null){
            synchronized (ProduceBoxSingle.class){
                if(instance==null){
                    instance=new ProduceBox();
                }
            }
        }
        return instance;
    }

}
