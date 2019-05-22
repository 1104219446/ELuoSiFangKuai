package com.example.administrator.eluosifangskuai.model;

import android.os.Message;
import android.widget.TextView;

import com.example.administrator.eluosifangskuai.Config;

/**
 * Created by ZZK on 2018/2/3.
 */

public class ScoreModel {

    //游戏当前分数
    public int score=0;
    //游戏最高分
    public int maxScore=0;
    //当前游戏难度
    public int level=0;
    //根据消掉的函数加分
    public void addScore(int lines) {
        if (lines > 0) {
            score += lines + lines - 1;
        }
    }
    //更新最大分数
    public void updateScoreMax() {
        if(maxScore==0){
            //从本地加载数据
            maxScore=(int) Config.sp.getInt("scoreMax",0);
        }
        if(score>maxScore){
            maxScore=score;
            //储存在本地
            Config.editor.putInt("scoreMax",maxScore);
            Config.editor.commit();
        }
    }
    //显示当前分数
    public void showNowScore(TextView textView) {
        textView.setText(""+score);
    }
    //显示最大分数
    public void showMaxScore(TextView textView){
        textView.setText(""+maxScore);
    }
    //检测是否增加难度，增加返回true
    public boolean checkaddLevel() {
        //每五分升级
        int templevel=score/5;
        if(level<=22) {
            if (templevel != level) {
                level = templevel;
                return true;
            }
        }
        return false;
    }
}
