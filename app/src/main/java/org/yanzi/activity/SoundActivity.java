package org.yanzi.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.yanzi.playcamera.R;
import org.yanzi.util.SoundUtil;

import java.util.HashMap;
import java.util.Map;

public class SoundActivity extends Activity {

    SoundUtil soundUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        soundUtil = new SoundUtil(this, R.raw.start, R.raw.success, R.raw.failure);

    }

    public void start(View v){
        soundUtil.play(0);
    }

    public void success(View v){
       soundUtil.play(1);
    }

    public void failure(View v){
       soundUtil.play(2);
    }

}
