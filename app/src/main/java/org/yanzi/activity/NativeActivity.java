package org.yanzi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.yanzi.playcamera.R;
import org.yanzi.util.JniTool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NativeActivity extends Activity {

    TextView tv, tv1;
    ImageView iv1, iv2;
    ProgressBar pb;
    float result;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        pb = (ProgressBar) findViewById(R.id.pb);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123){
                    tv.setText("比对结果："+result);
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                result = JniTool.faceVeri2("a1.jpg", "b1.jpg");
                handler.sendEmptyMessage(0x123);
            }
        }).start();

        //tv1.setText(JniTool.faceVeri1()+"");
        iv1.setImageBitmap(BitmapFactory.decodeFile("/data/data/org.yanzi.playcamera/files/a1.jpg"));
        iv2.setImageBitmap(BitmapFactory.decodeFile("/data/data/org.yanzi.playcamera/files/b1.jpg"));
        //saveBitmap(BitmapFactory.decodeFile("/storage/emulated/0/face/b1.jpg"), "test1111.jpg");
    }


    /**
     * 保存Bitmap到SD卡中
     * @param bitmap 位图对象
     * @param name 图片名称
     */
    public void saveBitmap(Bitmap bitmap, String name){
        FileOutputStream fos = null;
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fos = new FileOutputStream("/storage/emulated/0/face/"+name);
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert stream != null;
                stream.close();
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
