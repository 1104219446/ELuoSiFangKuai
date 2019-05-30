package com.example.administrator.eluosifangskuai.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.example.administrator.eluosifangskuai.R;
import com.example.administrator.eluosifangskuai.single.MediaPlayerSingle;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ZZK on 2018/2/5.
 */

public class MusicModel {
    //音乐播放器
    private MediaPlayer player=null;
    //音效播放器
    private SoundPool soundPool=null;
    //音效ID
    private HashMap<Integer,Integer> soundsID=null;

    //游戏音效资源
    public static final int ROTATE=0;
    public static final int CLEARLINES=1;
    public static final int CLICKBUTTON=2;
    public static final int GAMEOVER=3;
    public static final int BOOM=4;
    public static final int ADDLINES=5;
    public static final int LEVELUP=6;

    public MusicModel(Context context) {
        initPlayer(context);
        initMusics(context);
    }
    //初始化音乐播放器
    private void initMusics(Context context) {
        if(soundsID!=null&&soundPool!=null) {
            soundsID.put(0,soundPool.load(context,R.raw.rotate,1));
            soundsID.put(1,soundPool.load(context,R.raw.clearlines,1));
            soundsID.put(2,soundPool.load(context,R.raw.button,1));
            soundsID.put(3,soundPool.load(context,R.raw.gameover,1));
            soundsID.put(4,soundPool.load(context,R.raw.boom,1));
            soundsID.put(5,soundPool.load(context,R.raw.addlines,1));
            soundsID.put(6,soundPool.load(context,R.raw.levelup,1));
        }
    }
    //Android5.0判断
    @SuppressLint("UseSparseArrays")
    private void initPlayer(Context context) {
        //创建音乐播放器
        player=MediaPlayerSingle.getInstance(context);
        //设置循环播放
        player.setLooping(true);
        //整形可以直接用<>
        soundsID=new HashMap<>();
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(5);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }
        //当系统的SDK版本小于21时
        else {//设置最多可容纳5个音频流，音频的品质为5
            soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        }
    }
    //播放BGM
    public void playBgm() {
        player.start();
    }
    //播放ID号音效
    public void playSounds(int id){
        soundPool.play(soundsID.get(id),1,
                1,1,0,1);
    }
    //暂停BGM
    public void pauseBgm(){
        if(player.isPlaying()){
            player.pause();
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }


}
