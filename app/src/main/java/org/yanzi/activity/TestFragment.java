package org.yanzi.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.yanzi.playcamera.R;
import org.yanzi.util.AssetsUtil;


/**
 * Created by asus on 2017/2/17.
 */

public class TestFragment extends DialogFragment {

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    int position;
    String path = null;
    Button btn_play;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, container, false);

        surfaceView = (SurfaceView) v.findViewById(R.id.media_player);
        mediaPlayer = new MediaPlayer();
        surfaceHolder = surfaceView.getHolder();

        btn_play = (Button) v.findViewById(R.id.play);


        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(position > 0){
                    try {
                        //开始播放
                        if(!TextUtils.isEmpty(path)){
                            play(path);
                            //并直接从指定位置开始播放
                            mediaPlayer.seekTo(position);
                            position=0;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(AssetsUtil.DIR1+"/test.mp4");
            }
        });

        return v;
    }

    private void play(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer
                    .setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置需要播放的视频
            mediaPlayer.setDataSource(path);
            //把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            //播放
            mediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
