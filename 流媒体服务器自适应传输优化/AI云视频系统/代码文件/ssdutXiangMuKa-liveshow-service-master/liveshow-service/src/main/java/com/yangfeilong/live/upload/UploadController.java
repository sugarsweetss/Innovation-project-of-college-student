package com.yangfeilong.live.upload;

import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import com.yangfeilong.live.common.controller.BaseController;

import java.io.File;
import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class UploadController extends BaseController{
    private UploadService srv=new UploadService();

    public void index(String videoName, String description, long duration){
        List<UploadFile> list=getFiles();
        videoName=getPara("video_name");
        description=getPara("description");
        duration=getParaToLong("duration");
        if(list.size()!=2){
            renderJson(Ret.fail());
        }
        File videoFile=list.get(0).getFile();
        File coverFile=list.get(1).getFile();
        System.out.println(videoFile.getName());
        String token=getToken();
        renderJson(srv.upload(videoFile,coverFile,videoName,token,description,duration));
    }
}
