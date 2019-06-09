package com.live.feilong.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gavin on 17-9-28.
 */

public class FeilongHelper {

    public static String postURL(String url, HashMap<String,String> map){
        OkHttpClient client=new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        Set<String> keys=map.keySet();
        for(String key:keys){
            builder.add(key,map.get(key));
        }
        RequestBody body=builder.build();
        Request request=new Request.Builder().url(url).post(body).build();
        try {
            Response response=client.newCall(request).execute();
            String str=response.body().string();
            Log.i("liveshow",str);
            if(response.isSuccessful()) {
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getURL(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else{
                return "no";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no";
    }

    public static String analyseErrorResult(String result){
        try{
            HashMap<String,Object> maps= JSON.parseObject(result,new TypeReference<HashMap<String, Object>>(){});
            if(maps.get("state").toString().equalsIgnoreCase("ok")){
                return "ok";
            }
            if(maps.containsKey("error")){
                String error=maps.get("error").toString();
                return error;
            }
            if(maps.containsKey("wrong")){
                String wrong=maps.get("wrong").toString();
                return wrong;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void sendMessage(Handler handler,int what){
        Message message=new Message();
        message.what=what;
        handler.sendMessage(message);
    }
    public void sendMessage(Handler handler,int what,int arg1){
        Message message=new Message();
        message.what=what;
        message.arg1=arg1;
        handler.sendMessage(message);
    }
    public void sendMessage(Handler handler,int what,int arg1,int arg2){
        Message message=new Message();
        message.what=what;
        message.arg1=arg1;
        message.arg2=arg2;
        handler.sendMessage(message);
    }


}











