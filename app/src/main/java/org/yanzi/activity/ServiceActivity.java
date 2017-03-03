package org.yanzi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.yanzi.playcamera.R;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = new Intent();
        intent.setClassName("com.huashi.lua.a100u", "com.huashi.lua.a100u.PhotoIntentService");
        startService(intent);

    }
}
