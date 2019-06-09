package com.yangfeilong.live.delete;

import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-25.
 */
public class DeleteController extends BaseController{
    DeleteService srv=new DeleteService();
    public void index(){
        System.out.println(getPara("token"));
        System.out.println(getPara("video_id"));
        int video_id= Integer.parseInt(getPara("video_id"));
        String token=getToken();
        renderJson(srv.deleteVideo(video_id,token));
    }
}
