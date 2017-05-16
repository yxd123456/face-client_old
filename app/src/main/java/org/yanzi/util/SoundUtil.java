package org.yanzi.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/1/2.
 */

public class SoundUtil {

    private SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
    //存放音效的HashMap
    private Map<Integer,Integer> map = new HashMap<>();

    public SoundUtil(Context ctx, int... sounds){
        for (int i = 0; i < sounds.length; i++) {
            map.put(i,soundPool.load(ctx, sounds[i],i+1));
        }
    }

    public void play(int index){
        soundPool.play(
                map.get(index),//声音资源
                10,//左声道
                10,//右声道
                9,//优先级
                0,//循环次数，0是不循环，-1是一直循环
                1);//回放速度，0.5~2.0之间，1为正常速度
    }

    public void playLouder(int index){
        soundPool.play(
                map.get(index),//声音资源
                90,//左声道
                90,//右声道
                9,//优先级
                0,//循环次数，0是不循环，-1是一直循环
                1);//回放速度，0.5~2.0之间，1为正常速度
    }


}
