package com.huashi.lua.a100u;

import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huashi.otg.sdk.HsOtgService;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.huashi.otg.sdk.HsOtgService;
import com.huashi.otg.sdk.HSInterface;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PhotoIntentService extends IntentService {

    HSInterface HSinterface;
    String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
    public PhotoIntentService() {
        super("PhotoIntentService");
    }
    MyConn conn;
    Intent service;
    private GoogleApiClient client;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0x123){
                System.out.println("开始了啊"+ Thread.currentThread().getName());
                if (HSinterface == null){
                    System.out.println("中断了");
                    return;
                }
                int ret = HSinterface.Authenticate();
                System.out.println("ret的值是"+ret);
                if (ret == 1){
                    System.out.println("卡认证成功");
                }else if (ret == 2){
                    System.out.println("卡认证失败");
                }else if (ret == 0){
                    System.out.println("未连接");
                }
                if (HSinterface == null)
                    return;
                int ret1 = HSinterface.ReadCard();
                if (ret1 == 1){
                    //成功
                    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式
                    System.out.println("姓名：" + HsOtgService.ic.getPeopleName());
                    System.out.println("身份号码：" + HsOtgService.ic.getIDCard());

                    System.out.println("读卡");

                    try {
                        System.out.println("0");
                        ret1 = HSinterface.Unpack();// 照片解码
                        Log.d("Face", "*******"+ret1);
                        if (ret1 != 0) {// 读卡失败
                            System.out.println("读卡失败");
                            return;
                        }
                        FileInputStream fis = new FileInputStream(filepath + "/zp.bmp");
                        Bitmap bmp = BitmapFactory.decodeStream(fis);
                        fis.close();
                        System.out.println("读卡cg");
                        MainActivity.saveBitmap(bmp, "OnMyGod.jpg");
                    } catch (FileNotFoundException e) {
                        System.out.println("1");
                    } catch (IOException e) {
                        System.out.println("2");
                    }catch (Exception e)
                    {
                        System.out.println("3");
                    }finally {
                        System.out.println("4");
                        HsOtgService.ic = null;
                        return;
                    }
                }else {
                    //失败
                    System.out.println("读卡失败");
                }
            }
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        service = new Intent(PhotoIntentService.this, HsOtgService.class);
        conn = new MyConn();
        bindService(service, conn, Service.BIND_AUTO_CREATE);

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.huashi.lua.a100u/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

        while (true){

            handler.sendEmptyMessage(0x123);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {




            HSinterface = (HSInterface) service;
            int i = 2;
            while (i > 0) {
                i--;
                int ret = HSinterface.init();
                if (ret == 1) {
                    i = 0;
                    System.out.println("已连接");
                    System.out.println(HSinterface.GetSAM());
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            conn = null;
            HSinterface = null;
        }
    }




}
