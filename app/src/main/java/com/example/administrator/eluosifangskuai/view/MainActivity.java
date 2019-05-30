package com.example.administrator.eluosifangskuai.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.eluosifangskuai.Config;
import com.example.administrator.eluosifangskuai.R;
import com.example.administrator.eluosifangskuai.control.GameControl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //游戏区域组件
    SurfaceView gamePanel;
    //下一块区域组件
    SurfaceView nextPanel;
    //游戏控制模型
    GameControl gameControl;
    //主线程Handle
    public Handler handler;
    //当前分数
    private TextView textNowScore;
    private TextView textMaxScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //获取读取储存卡权限
        isGrantExternalRW(this);
        //初始化UI Handler
        initHander();
        //实例化游戏控制器
        gameControl=new GameControl(handler,this);
        //初始化视图
        initView();
        //添加按钮监听器
        initLinstrner();
        gameControl.musicModel.playBgm();
    }
    //UI线程Hander，用于更新UI组件
    @SuppressLint("HandlerLeak")
    private void initHander() {
        if(handler==null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String type=(String)msg.obj;
                    if(type==null)
                        return;
                    if (type == "invalidate") {
                        //游戏界面刷新
                        gamePanel.invalidate();
                        //下一块界面刷新
                        nextPanel.invalidate();
                        //分数更新
                        gameControl.scoreModel.showMaxScore(textMaxScore);
                        gameControl.scoreModel.showNowScore(textNowScore);
                    }
                    else if(type.equals("pause")){
                        ((Button)findViewById(R.id.btnpause)).setText("暂停");
                    }
                    else if(type.equals("continue")) {
                        ((Button)findViewById(R.id.btnpause)).setText("继续");
                    }
                    else if(type.equals("fzopen")) {
                        ((Button)findViewById(R.id.btn_fz)).setText("关");
                    }
                    else if(type.equals("fzclose")) {
                        ((Button)findViewById(R.id.btn_fz)).setText("开");
                    }else if(type.equals("music_off")){
                        ((Button)findViewById(R.id.btn_music)).setText("开");
                    }else if(type.equals("music_on")){
                        ((Button)findViewById(R.id.btn_music)).setText("关");
                    }else if(type.equals("Levelup")) {
                        ((TextView) findViewById(R.id.text_level)).setText("" + gameControl.scoreModel.level);
                        if (gameControl.scoreModel.score != 0) {
                            Toast.makeText(MainActivity.this, "难度增加，加油", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
        }
    }
    //初始化按钮监听器
    private void initLinstrner() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_down).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        findViewById(R.id.btnstart).setOnClickListener(this);
        findViewById(R.id.btn_up).setOnClickListener(this);
        findViewById(R.id.btnpause).setOnClickListener(this);
        findViewById(R.id.btn_onedown).setOnClickListener(this);
        findViewById(R.id.btn_fz).setOnClickListener(this);
        findViewById(R.id.btn_music).setOnClickListener(this);
    }
    //初始化视图
    private void initView() {
        //获取分数视图
        textMaxScore=findViewById(R.id.text_maxscore);
        textNowScore=findViewById(R.id.text_nowscore);
        //实例化游戏区域
        initGamePanel();
        //实例化下一块预览区域
        initnextPanel();
        //初始化背景
        initBackground();

    }

    private void initBackground() {
        LinearLayout layoutBeside=findViewById(R.id.layoutBeside);
        //LinearLayout layoutBottom=findViewById(R.id.layoutBottom);
        layoutBeside.setBackgroundColor(Color.rgb(128,128,128));
        //layoutBottom.setBackgroundResource(R.drawable.bottom);
    }

    //实例化游戏区域
    private void initGamePanel() {
        FrameLayout layoutGame=findViewById(R.id.layoutGame);
        gamePanel=new SurfaceView(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                gameControl.getDrawControl().draw(canvas);
            }
        };
        //设置游戏区域大小
        gamePanel.setLayoutParams(new FrameLayout.LayoutParams(Config.XWHITH,Config.YHEIGHT));
        //gamePanel.setBackgroundColor(Color.GRAY);
        gamePanel.setBackgroundResource(R.drawable.view);
        //设置游戏区域间距
        layoutGame.setPadding(Config.PADDING,Config.PADDING,Config.PADDING,Config.PADDING);
        layoutGame.addView(gamePanel);
        //设置信息区域间距
        LinearLayout layoutInfo=findViewById(R.id.layoutInfo);
        layoutInfo.setPadding(0,Config.PADDING,Config.PADDING,Config.PADDING);
    }
    //实例化下一块预览区域
    private void initnextPanel() {
        nextPanel=new SurfaceView(this){
            @Override
            protected void onDraw(Canvas canvas) {
                gameControl.getDrawControl().drawNext(canvas,nextPanel.getWidth());
            }
        };
        nextPanel.setLayoutParams(new FrameLayout.LayoutParams(-1,300));
        nextPanel.setBackgroundColor(Color.rgb(128,128,128));
        FrameLayout layoutNext=findViewById(R.id.layoutNext);
        layoutNext.addView(nextPanel);
    }
    //点击事件
    @Override
    public void onClick(View view) {

        gameControl.onClick(view.getId());
        gamePanel.invalidate();
        nextPanel.invalidate();

    }
    //返回键停止播放音乐
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            gameControl.musicModel.pauseBgm();
        }
        return super.onKeyDown(keyCode, event);
    }
    //重启播放音乐
    @Override
    protected void onRestart() {
        super.onRestart();
        gameControl.musicModel.playBgm();
    }

    //安卓6.0以上读取内存文件需要手动请求权限
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{

                    //内存卡读写
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                    } else {
                        Toast.makeText(this, "请给本程序读取储存卡权限，否则游戏无法更新最高分", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                }
            }
        }
    }
}
