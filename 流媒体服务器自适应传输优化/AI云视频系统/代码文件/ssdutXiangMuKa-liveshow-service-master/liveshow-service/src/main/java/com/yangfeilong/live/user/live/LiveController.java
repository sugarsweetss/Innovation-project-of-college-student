package com.yangfeilong.live.user.live;

import com.jfinal.core.paragetter.Para;
import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-29.
 */
public class LiveController extends BaseController{
    private LiveService srv=new LiveService();
    public void liveon(){
        renderJson(srv.liveon(getToken()));
    }
    public void liveoff(){
        renderJson(srv.liveoff(getToken()));
    }
}
