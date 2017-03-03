package org.yanzi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowContentFrameStats;
import android.widget.Toast;

import org.yanzi.camera.preview.CameraSurfaceView1;
import org.yanzi.playcamera.R;
import org.yanzi.util.FileUtil;

import java.io.File;

public class MeidaActivity extends Activity {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    int position;
    String path = "/data/data/org.yanzi.playcamera/files/data/test.mp4";
    CameraSurfaceView1 view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meida);

        view1 = (CameraSurfaceView1) findViewById(R.id.csv);

        surfaceView = (SurfaceView) findViewById(R.id.media_player);
        mediaPlayer = new MediaPlayer();
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //if(position > 0){
                    try {
                        //开始播放
                        if(!TextUtils.isEmpty(path)){
                            play1(path);
                            //并直接从指定位置开始播放
                            //mediaPlayer.seekTo(position);
                           // position=0;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception  
                    }
                //}
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    private void play1(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer
                    .setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置需要播放的视频
            mediaPlayer.setDataSource(path);
            //把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            //播放
            mediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void play(View v){
        //if(!TextUtils.isEmpty(path)){
            play1("/data/data/org.yanzi.playcamera/files/data/test.mp4");
        //}
    }

    public void pause(View v){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }

    public void stop(View v){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void select(View v){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("video/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent,1);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            path = FileUtil.getPath(this, uri);
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            play1(path);
        }
    }

    @Override
    protected void onPause() {
        //先判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            //如果正在播放我们就先保存这个播放位置
            position=mediaPlayer.getCurrentPosition()
            ;
            mediaPlayer.stop();
        }
        super.onPause();
    }



}
