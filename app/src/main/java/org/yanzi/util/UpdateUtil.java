package org.yanzi.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by asus on 2017/2/16.
 */

public class UpdateUtil {

    private static String getVersionName(Context ctx) throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        return packInfo.versionName;
    }

}
