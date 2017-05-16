package org.yanzi.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import org.yanzi.playcamera.R;
import org.yanzi.util.AssetsUtil;
import org.yanzi.util.JniTool;

import java.io.File;

public class StartActivity extends Activity {

    static {
        System.loadLibrary("face-libs");
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                startActivity(new Intent(StartActivity.this, CameraActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        }
    };

    TextView tv_start;
    TranslateAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Log.d("Hope", "1111111111111");

        if(!(new File("/data/data/org.yanzi.playcamera/files/data").exists())
                ||new File("/data/data/org.yanzi.playcamera/files/faceVeriConfig.yml").exists()){
            AssetsUtil.copy(StartActivity.this);
        }

        Log.d("Hope", "22222222222222");


        tv_start = (TextView) findViewById(R.id.tv_start);
        animation = new TranslateAnimation(0, 60, 0, 0);
        animation.setRepeatCount(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(2000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                tv_start.startAnimation(animation);
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                JniTool.init();

//                if(num == -10){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new AlertDialog.Builder(StartActivity.this)
//                                    .setTitle("提示")
//                                    .setMessage("超出使用期限，初始化失败！").create().show();
//                        }
//                    });
//                }else {
                    handler.sendEmptyMessage(0x123);
                //}
            }
        }).start();

    }
}
