package org.yanzi.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by asus on 2017/1/17.
 */

public class RectSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder surfaceHolder;

    Paint paint;

    public Canvas canvas;


    public RectSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public RectSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawRect(0, 0, 200, 200, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
