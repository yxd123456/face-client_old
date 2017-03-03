package org.yanzi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.yanzi.playcamera.R;
import org.yanzi.util.JniTool;
import org.yanzi.util.Util;

public class DrawRectActivity extends Activity {

    ImageView img;

    static {
        System.loadLibrary("face-libs");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_rect);

        Log.d("Hope", "故事"+JniTool.test());
        Util.strToArr(JniTool.test());

        img = (ImageView) findViewById(R.id.iv);
        Bitmap photo = BitmapFactory.decodeResource(getResources(), R.mipmap.start1);
        Bitmap tempBitmap = photo.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(tempBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(5);  //线的宽度
        canvas.drawRect(0, 0, 1, 1, paint);

        img.setImageBitmap(tempBitmap);
    }

}
