package com.huashi.lua.a100u;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huashi.otg.sdk.HsOtgService;
import com.huashi.otg.sdk.HSInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    MyConn conn;
    HSInterface HSinterface;
    Intent service;
    String filepath = "";

    private TextView tv_sam, tv_info, tv_statu;
    private ImageView iv_photo;
    private Button bt_conn, bt_find, bt_read;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void saveBitmap(Bitmap bitmap, String name){
        FileOutputStream fos = null;
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fos = new FileOutputStream("/data/data/com.huashi.lua.a100u/test/"+name);
            fos.write(byteArray);
            System.out.println("Success Save!!!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("failure Save!!!");
        } finally {
            try {
                assert stream != null;
                stream.close();
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("end!!!");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.1); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.1); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        initView();
        initData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        tv_sam = (TextView) findViewById(R.id.sam);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_statu = (TextView) findViewById(R.id.statu);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        bt_conn = (Button) findViewById(R.id.conn);
        bt_find = (Button) findViewById(R.id.find);
        bt_read = (Button) findViewById(R.id.read);
    }

    private void initData() {
        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
        service = new Intent(MainActivity.this, HsOtgService.class);
        bt_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (conn == null) {
                    conn = new MyConn();
//                }
                bindService(service, conn, Service.BIND_AUTO_CREATE);

            }
        });


        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HSinterface == null)
                    return;
                int ret = HSinterface.Authenticate();
                if (ret == 1){
                    tv_info.setText("卡认证成功");
                }else if (ret == 2){
                    tv_info.setText("卡认证失败");
                }else if (ret == 0){
                    tv_info.setText("未连接");
                }

            }
        });

        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HSinterface == null)
                    return;
                tv_info.setText("读卡……");
                iv_photo.setImageBitmap(null);
                int ret = HSinterface.ReadCard();

                if (ret == 1){
                    //成功
                    Log.d("TT", "------------"+(HsOtgService.ic==null));
                    byte[] fp = new byte[1024];
                    fp = HsOtgService.ic.getFpDate();
                    String m_FristPFInfo = "";
                    String m_SecondPFInfo = "";

                    if (fp[4] == (byte)0x01) {
                        m_FristPFInfo = String.format("指纹  信息：第一枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[5]), fp[6]);
                    } else {
                        m_FristPFInfo = "身份证无指纹 \n";
                    }
                    if (fp[512 + 4] == (byte)0x01) {
                        m_SecondPFInfo = String.format("指纹  信息：第二枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[512 + 5]),
                                fp[512 + 6]);
                    } else {
                        m_SecondPFInfo = "身份证无指纹 \n";
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式
                    tv_info.setText("姓名：" + HsOtgService.ic.getPeopleName() + "\n" + "性别：" + HsOtgService.ic.getSex() + "\n" + "民族：" + HsOtgService.ic.getPeople()
                            + "\n" + "出生日期：" + df.format(HsOtgService.ic.getBirthDay()) + "\n" + "地址：" + HsOtgService.ic.getAddr() + "\n" + "身份号码："
                            + HsOtgService.ic.getIDCard() + "\n" + "签发机关：" + HsOtgService.ic.getDepartment() + "\n" + "有效期限：" + HsOtgService.ic.getStrartDate()
                            + "-" + HsOtgService.ic.getEndDate() + "\n"+m_FristPFInfo+"\n"+m_SecondPFInfo);

                    try {
                        ret = HSinterface.Unpack();// 照片解码
                        if (ret != 0) {// 读卡失败

                            return;
                        }
                        FileInputStream fis = new FileInputStream(filepath + "/zp.bmp");
                        Bitmap bmp = BitmapFactory.decodeStream(fis);
                        fis.close();
                        iv_photo.setImageBitmap(bmp);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "头像不存在！", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // TODO 自动生成的 catch 块
                        Toast.makeText(getApplicationContext(), "头像读取错误", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "头像解码失败", Toast.LENGTH_SHORT).show();
                    }finally {
                        HsOtgService.ic = null;
                    }
                }else {
                    //失败
                    tv_info.setText("读卡失败");
                    iv_photo.setBackgroundResource(R.mipmap.ic_launcher);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10000);
//                    ComponentName name = new ComponentName("org.yanzi.playcamera",
//                            "org.yanzi.activity.StartActivity");
//                    Intent intent = new Intent();
//                    intent.setComponent(name);
//                    startActivity(intent);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
                    tv_statu.setText("已连接");
                    tv_sam.setText(HSinterface.GetSAM());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HSinterface.unInit();
        unbindService(conn);
    }

    /**
     * 指纹 指位代码
     *
     * @param FPcode
     * @return
     */
    String GetFPcode(int FPcode) {
        switch (FPcode) {
            case 11:
                return "右手拇指";
            case 12:
                return "右手食指";
            case 13:
                return "右手中指";
            case 14:
                return "右手环指";
            case 15:
                return "右手小指";
            case 16:
                return "左手拇指";
            case 17:
                return "左手食指";
            case 18:
                return "左手中指";
            case 19:
                return "左手环指";
            case 20:
                return "左手小指";
            case 97:
                return "右手不确定指位";
            case 98:
                return "左手不确定指位";
            case 99:
                return "其他不确定指位";
            default:
                return "未知";
        }
    }
}
