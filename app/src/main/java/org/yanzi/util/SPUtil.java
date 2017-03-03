package org.yanzi.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.yanzi.mode.FaceData;

import java.util.List;

/**
 * Created by asus on 2017/2/16.
 */

public class SPUtil{

    private static final String SP_KEY = "upload_failure_data";
    private static final String EDITOR_KEY = "save";

    public static String save(Context ctx, List<FaceData> data){
        SharedPreferences sp = ctx.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        Log.d("OFFLINE", data.size()+"the saved data is" + jsonData);
        editor.putString(EDITOR_KEY ,jsonData);
        editor.apply();
        return jsonData;
    }

    public static List<FaceData> get(Context ctx){
        SharedPreferences preferences= ctx.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
        String data = preferences.getString(EDITOR_KEY, "NULL");
        Log.d("OFFLINE", "the getted data is" + data);
        Gson gson = new Gson();
        List<FaceData> list = gson.fromJson(data, new TypeToken<List<FaceData>>() {}.getType());
        return list;
    }


}
