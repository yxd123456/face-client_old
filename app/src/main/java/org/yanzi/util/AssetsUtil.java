package org.yanzi.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by asus on 2017/2/16.
 */

/**
 * http://www.open-open.com/lib/view/open1474425946759.html
 */
public class AssetsUtil {

    public static final String DIR1 = "/data/data/org.yanzi.playcamera/files";
    public static final String DIR2 = "/data/data/org.yanzi.playcamera/files/data";
    public static final String DIR3 = "/data/data/org.yanzi.playcamera/camera_img";

    public static void copy(Context ctx){
        File file1 = new File(DIR1);
        if(!file1.exists()){
            file1.mkdir();
        }
        File file2 = new File(DIR2);
        if(!file2.exists()){
            file2.mkdir();
        }
        File file3 = new File(DIR3);
        if(!file3.exists()){
            file3.mkdir();
        }

        AssetManager manager = ctx.getAssets();
        try {
            String[] files = manager.list("files");
            for (int i = 0; i < files.length; i++) {
                if(i==0){
                    String[] data = manager.list("files/data");
                    for (String name:data
                         ) {
                        Log.d("Test", name+"-------------");
                        InputStream is = manager.open("files/data/"+name);
                        FileOutputStream fos = new FileOutputStream(DIR2+"/"+name);
                        byte[] bytes = new byte[1024];
                        int read = 0;
                        while ((read = is.read(bytes))!=-1){
                            fos.write(bytes, 0, read);
                        }
                        fos.flush();
                        fos.close();
                    }
                }else if(i==1){
                    InputStream is = manager.open("files/"+files[1]);
                    FileOutputStream fos = new FileOutputStream(DIR1+"/"+files[1]);
                    byte[] bytes = new byte[1024];
                    int read = 0;
                    while ((read = is.read(bytes))!=-1){
                        fos.write(bytes, 0, read);
                    }
                    fos.flush();
                    fos.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
