package com.example.administrator.eluosifangskuai.single;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.administrator.eluosifangskuai.R;

/**
 * Describe：
 * Author:zzk
 * CreateTime:2019/5/27
 */
public class MediaPlayerSingle {
    private static volatile MediaPlayer playerInstance;

    private MediaPlayerSingle(){}
    //double Check
    public static MediaPlayer getInstance(Context context){
        if(playerInstance==null){
            synchronized (MediaPlayerSingle.class){
                if(playerInstance==null){
                    playerInstance=MediaPlayer.create(context, R.raw.bgm1);
                }
            }
        }
        return playerInstance;
    }
}
