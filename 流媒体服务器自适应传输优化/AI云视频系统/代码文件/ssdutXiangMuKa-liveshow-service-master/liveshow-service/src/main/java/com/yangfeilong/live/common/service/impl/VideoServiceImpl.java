package com.yangfeilong.live.common.service.impl;

import com.yangfeilong.live.common.model.Video;
import com.yangfeilong.live.common.service.VideoService;

import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class VideoServiceImpl implements VideoService{
    private static final Video dao=new Video().dao();
    public Video findVideoById(Object id) {
        return dao.findById(id);
    }

    public List<Video> findVideosByName(String name){
        String query="select * from l_video where video_name="+"'"+name+"'";
        System.out.println(query);
        return dao.find(query);
    }

    public Video findVideoByUIDandName(int userId,String videoName){
        if(videoName!=null){
            videoName="'"+videoName+"'";
        }
        String query="select * from l_video where video_name="+videoName+" and user_id="+userId;
        List<Video> list=dao.find(query);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    public boolean deleteVideoById(Object id) {
        try{
            dao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save(Video model) {
        try{
            model.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Video model) {
        try{
            model.update();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Video> getLocalVideoList() {
        return dao.find("select * from l_video");
    }
}
