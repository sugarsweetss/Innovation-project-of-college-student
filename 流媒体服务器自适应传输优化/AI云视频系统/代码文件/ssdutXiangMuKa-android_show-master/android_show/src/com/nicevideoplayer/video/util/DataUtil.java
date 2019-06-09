package com.nicevideoplayer.video.util;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nicevideoplayer.video.bean.Video;
import com.live.feilong.model.listView.baseModel.LiveVideoModel;
import com.live.feilong.model.listView.baseModel.LocalVideoModel;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaoJianjun on 2017/7/7.
 */

public class DataUtil {
    public static List<Video> getVideoListData() {
        List<Video> videoList = new ArrayList<>();
        String url="http://112.74.182.83/liveshow/video/getLocalVideoList";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        try{
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                String str=response.body().string();
                Log.i("test",str);
                LocalVideoModel LocalVideo= JSON.parseObject(str, LocalVideoModel.class);
                List<HashMap<String,Object>> list=LocalVideo.getLocalvideolist();
                for(HashMap<String,Object> video:list){
                    String nickName=getNickName(video.get("user_id"));
                    String urlNickName= URLEncoder.encode(nickName,"utf-8");
                    String cover=URLEncoder.encode(video.get("cover").toString(),"UTF-8");
                    String description=video.get("description").toString();
                    String videoName=video.get("video_name").toString();
                    String videoType=video.get("video_format").toString();
                    String finalShowTitle="播放:"+videoName+"."+videoType+"\n"+description+"\n上传者:"+nickName;
                    String photo_url="http://112.74.182.83/androidSrc/localvideo/"+urlNickName+"/"+cover;
                    String videoUrl="http://112.74.182.83:8080/"+urlNickName+"/"+videoName+"."+videoType;
                    int dura= (int) video.get("duration");
                    long duration=dura*1000;
                    Log.i("livetest","local:"+videoUrl);
                    Log.i("livetest","image:"+photo_url);
                    videoList.add(new Video(finalShowTitle,duration,photo_url,videoUrl));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return videoList;
    }


    public static List<Video> getLiveListData(){
        List<Video> videoList = new ArrayList<>();
        String url="http://112.74.182.83/liveshow/video/getLiveVideoList";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        try{
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                String str=response.body().string();
                Log.i("test",str);
                LiveVideoModel LocalVideo= JSON.parseObject(str, LiveVideoModel.class);
                List<HashMap<String,Object>> list=LocalVideo.getLivevideolist();
                for(HashMap<String,Object> video:list){
                    String userName=video.get("nick_name").toString();
                    String finalShowTitle="直播中:"+userName;
                    String photo_url="";
                    String videoUrl="rtmp://112.74.182.83/live/"+userName;
                    Log.i("livetest","url:"+videoUrl);
                    videoList.add(new Video(finalShowTitle,1,photo_url,videoUrl));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return videoList;
    }

    public static String getNickName(Object id){
        OkHttpClient client=new OkHttpClient();
        String url="http://112.74.182.83/liveshow/user/getNickName?user_id="+id;
        Request request=new Request.Builder().url(url).build();
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                HashMap<String,String> maps=JSON.parseObject(response.body().string(), new TypeReference<HashMap<String, String>>(){}.getType());
                return maps.get("nickname");
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
