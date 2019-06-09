package com.yangfeilong.live.video.index;

import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;
import com.yangfeilong.live.common.service.impl.VideoServiceImpl;

/**
 * Created by gavin on 17-9-20.
 */
public class VideoIndextService {
    private VideoServiceImpl videoService=new VideoServiceImpl();
    private UserServiceImpl userService=new UserServiceImpl();
    public Ret getLocalVideoList(){
        return Ret.ok("localvideolist",videoService.getLocalVideoList());
    }

    public Ret getLiveVideoList(){
        return Ret.ok().set("livevideolist",userService.findLiveUser());
    }
}
