package com.example.administrator.eluosifangskuai.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.eluosifangskuai.Config;
import com.example.administrator.eluosifangskuai.R;
import com.example.administrator.eluosifangskuai.model.BoxModel;
import com.example.administrator.eluosifangskuai.model.MapsModel;
import com.example.administrator.eluosifangskuai.model.MusicModel;
import com.example.administrator.eluosifangskuai.model.ScoreModel;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 游戏控制器
 * Created by ZZK on 2018/2/2.
 */

public class GameControl {
    //Acitvity的上下文
    private Context context;
    //方块大小
    private int boxSize;
    //地图模型
    private MapsModel mapsModel=null;
    //方块模型
    private BoxModel boxModel=null;
    //分数模型
    public ScoreModel scoreModel=null;
    //音乐模型
    public MusicModel musicModel=null;
    //绘画控制器
    private DrawControl drawControl=null;

    //自动下落线程
    public Thread downthread;
    //播放音乐
    private boolean ismusic_on=true;
    //游戏过程变量
    private boolean isPause=false;
    private boolean isover=false;
    private boolean  isopenfz=true;
    //UI Handler用与更新UI组件
    private Handler  handler=null;
    //游戏时间管理相关
    Timer timer=null;
    TimerTask timerTask=null;
    //30秒
    int time_interval=30;
    //当前时间
    int timesecond=0;

    public GameControl(Handler handler,Context context) {
        this.handler=handler;
        this.context=context;
        this.initDate(context);
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                if(!isPause&&!isover) {
                     timesecond+=1;
                    if(timesecond>=1000000){
                     timesecond=0;
                    }
                }
            }
        };
    }
    /** 开始游戏 **/
    private void startGame() {
        if(downthread==null) {
            //初始化线程
            initThread();
            boxModel.newboxs();
            isPause=false;
            scoreModel.updateScoreMax();
        } else {
            //如果游戏结束
            gameOverLogic();
        }
    }
    //检测是否升级
    private void checkUpgrade(){
        if(scoreModel.checkaddLevel()){
            //播放音效
            musicModel.playsounds(MusicModel.LEVELUP);
            Message msg=new Message();
            msg.obj="Levelup";
            handler.sendMessage(msg);
        }
    }
    //加分检测
    private void checkScoreAdd(){
        int lines=mapsModel.cleanLine();
        if(lines!=0) {
            //播放消行音效
            musicModel.playsounds(MusicModel.CLEARLINES);
        }
        //加分
        scoreModel.addScore(lines);
    }

    //初始化线程
    private void initThread(){
        downthread=new Thread() {
            @Override
            public void run() {
                //分值初始化
                scoreModel.score=0;
                //关卡初始化
                scoreModel.level=0;
                //定时器初始化
                timer.schedule(timerTask,time_interval*1050, 1050);
                while(true) {
                    SystemClock.sleep(600-scoreModel.level*20);
                    if(isover&&musicModel.player.isPlaying()){
                        musicModel.Pausebgm();
                    }
                    //没死的逻辑
                    if(!isover) {
                        //检测是否加行
                        checkaddlines();
                        //检测是否游戏升级
                        checkUpgrade();
                        //加分检测
                        checkScoreAdd();
                        //方块下落
                        boxsDownLogic();
                    }
                }
            }
        };
        downthread.start();
    }

    //方块下落逻辑处理
    private void boxsDownLogic(){
        if (!isPause) {
            //下落一次
            if (!boxModel.move(0, 1, mapsModel)) {
                //获取当前方块颜色
                int colorid=boxModel.boxtype;
                for (int i = 0; i < boxModel.boxs.length; i++) {
                    mapsModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y]
                            = colorid;
                }
                boxModel.newboxs();
                drawControl.updatepreBoxs();
            }
            isover=checkgameover();
            Message msg=new Message();
            msg.obj="invalidate";
            if(handler!=null)
                handler.sendMessage(msg);
        }
    }

    //游戏结束逻辑
    private void gameOverLogic(){
        if(isover) {
            if(!musicModel.player.isPlaying()&&ismusic_on){
                musicModel.playbgm();
            }
            //初始化地图
            mapsModel.cleanMaps();
            boxModel.newboxs();
            isover = false;
            isPause = false;
            scoreModel.score=0;
            scoreModel.level=0;
            Message msg=new Message();
            msg.obj="Levelup";
            handler.sendMessage(msg);
            scoreModel.updateScoreMax();
            timesecond=0;
        }
    }

    //检测是否加一行
    private void checkaddlines() {
        if(!isover&&!isPause){
            if(timesecond!=0&&timesecond%time_interval==0){
                timesecond++;
                //播放音效并加一行
                musicModel.playsounds(MusicModel.ADDLINES);
                mapsModel.addLines();
                //更新预览方块
                drawControl.updatepreBoxs();
            }
        }
    }

    //下移方块
    public boolean moveButton() {
        if(boxModel.boxs==null)
            return false;
        //移动成功 不作处理
        if(boxModel.move(0,1,mapsModel)) {
            return true;
        }
        //获取当前方块颜色
        int colorid=boxModel.boxtype;
        //移动失败，堆积处理
        for(int i=0;i<boxModel.boxs.length;i++) {
            mapsModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y]=colorid;
        }
        //行数
        int lines;
        //消行
        lines=mapsModel.cleanLine();
        if(lines!=0){
            //播放消行音效
            musicModel.playsounds(MusicModel.CLEARLINES);
        }
        //加分
        scoreModel.addScore(lines);
        //更新最高分
        scoreModel.updateScoreMax();
        //生成新方块
        boxModel.newboxs();
        //更新预览方块
        drawControl.updatepreBoxs();
        //游戏结束检测
        isover=checkgameover();
        return false;
    }

    //检测死亡
    public boolean checkgameover() {
        for(int i=0;i<boxModel.boxs.length;i++) {
            if(mapsModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y]>=0) {
                musicModel.playsounds(MusicModel.GAMEOVER);
                return true;
            }
        }
        return false;
    }
    //设置暂停
    private void setPause() {
        if(!isover) {
            isPause = !isPause;
            Message msg = new Message();
            if (isPause) {
                msg.obj = "continue";
            } else {
                msg.obj = "pause";
            }
            handler.sendMessage(msg);
        }
    }
    //初始化数据
    @SuppressLint("CommitPrefEdits")
    private void initDate(Context context) {
        //获取屏幕宽度
        int width=getScreenWidth(context);
        //设置游戏区域宽高
        Config.XWHITH=width*2/3;
        Config.YHEIGHT=width*4/3;
        //设置间距
        Config.PADDING=Config.XWHITH/Config.SPLIT_PADDING;
        //设置本地数据储存器
        Config.sp=context.getSharedPreferences("scoreMax",Context.MODE_PRIVATE);
        //设置本地数据编辑器
        Config.editor=Config.sp.edit();
        //设置单个方块的大小
        boxSize=Config.XWHITH/ Config.MAPX;
        //实例化boxModel
        boxModel=new BoxModel(boxSize);
        //实例化mapsModel
        mapsModel=new MapsModel( Config.YHEIGHT,Config.XWHITH,boxSize);
        //实例化drawControl
        drawControl=new DrawControl(boxModel,mapsModel);
        //实例化分数模型
        scoreModel=new ScoreModel();
        //实例化音乐模型
        musicModel=new MusicModel(context);


    }
    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        WindowManager wm=(WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    //点击事件
    public void onClick(int id) {
        if(id!=R.id.btn_up){
            //播放按钮音效
            musicModel.playsounds(MusicModel.CLICKBUTTON);
        }
        switch (id) {
            case R.id.btn_down:
                if(isPause||isover)
                    return;
                while(moveButton()) {
                }
                break;
            case R.id.btn_left:
                if(isPause||isover)
                    return;
                boxModel.move(-1,0,mapsModel);
                break;
            case R.id.btn_right:
                if(isPause||isover)
                    return;
                boxModel.move(1,0,mapsModel);
                break;
            case R.id.btn_onedown:
                if(isPause||isover)
                    return;
                boxModel.move(0,1,mapsModel);
                break;
            case R.id.btn_up:
                //播放旋转音效
                musicModel.playsounds(MusicModel.ROTATE);
                if(isPause||isover)
                    return;
                boxModel.rotate(mapsModel);
                break;
            case R.id.btnstart:
                startGame();
                Toast.makeText(context, "达到150分有惊喜，加油哦", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnpause:
                setPause();
                break;
            case R.id.btn_fz:
                setfz();
                break;
            case R.id.btn_music:
                ismusic_on=!ismusic_on;
                Message msg=new Message();
                if(musicModel.player.isPlaying()||!ismusic_on) {
                    musicModel.Pausebgm();
                    msg.obj="music_off";
                }else{
                    if(!isover&&ismusic_on) {
                        musicModel.playbgm();
                    }
                    msg.obj="music_on";
                }
                handler.sendMessage(msg);
               break;
        }
        drawControl.updatepreBoxs();
    }
    //设置辅助线
    private void setfz() {
        drawControl.setIsopenfz(!drawControl.isIsopenfz());
        Message msg=new Message();
        msg.obj=isopenfz?"fzclose":"fzopen";
        handler.sendMessage(msg);
    }

    public DrawControl getDrawControl() {
        return drawControl;
    }
}
