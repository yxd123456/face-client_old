package org.yanzi.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.yanzi.mode.User;
import org.yanzi.playcamera.R;
import org.yanzi.util.Util;

import java.io.File;

public class DbActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db2);

        Log.d("Face", "存储在吗"+new File("/data/data/org.yanzi.playcamera/camera_img/朱洁尔.jpg").exists());
        ImageView iv  = (ImageView) findViewById(R.id.iv);
        iv.setImageBitmap(BitmapFactory.decodeFile("/data/data/org.yanzi.playcamera/camera_img/朱洁尔.jpg"));
        DbUtils db = DbUtils.create(this);
        User user = new User(); //这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
        user.setEmail("wyouflf@qq.com");
        user.setName("wyouflf");
        try {
            db.save(user); // 使用saveBindingId保存实体时会为实体的id赋值
            System.out.println("保存成功");
        } catch (DbException e) {
            e.printStackTrace();
            System.out.println("保存失败");
        }

        try {
            User user1 = db.findFirst(User.class);
            if(user1 != null){
                System.out.println(user1.getName()+"\n"+user1.getEmail());
            } else {
                System.out.println("查不到");
            }
        } catch (DbException e) {
            e.printStackTrace();
            System.out.println("... ...");
        }

    }
}
