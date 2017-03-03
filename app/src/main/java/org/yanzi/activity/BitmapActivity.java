package org.yanzi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.yanzi.playcamera.R;
import org.yanzi.util.JniTool;

public class BitmapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.face);
        Log.d("Test", "1 "+System.currentTimeMillis());
        int[] px = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(px, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        for (int i = 0; i < px.length; i++) {
//            int r = Color.red(px[i]);
//            int g = Color.green(px[i]);
//            int b = Color.blue(px[i]);
//            Log.d("Test", r + " "+g+" "+b);
//        }
        //JniTool.testRGB(px, bitmap.getWidth(), bitmap.getHeight());
        Log.d("Test", "2 "+System.currentTimeMillis());

    }
}
