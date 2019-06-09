package com.yangfeilong.live.video.index;

import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-20.
 */
public class VideoIndexController extends BaseController{
    private VideoIndextService videoIndextService=new VideoIndextService();
    public void getLocalVideoList(){
        renderJson(videoIndextService.getLocalVideoList());
    }
    public void getLiveVideoList(){
        Ret ret=videoIndextService.getLiveVideoList();
        renderJson(ret);
    }
}
