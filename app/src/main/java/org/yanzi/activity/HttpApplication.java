package org.yanzi.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.yanzi.mode.FaceData;
import org.yanzi.playcamera.R;
import org.yanzi.util.AssetsUtil;
import org.yanzi.util.NetUtil;
import org.yanzi.util.SPUtil;
import org.yanzi.util.Util;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class HttpApplication extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_application);
        AssetsUtil.copy(this);
    }
}
