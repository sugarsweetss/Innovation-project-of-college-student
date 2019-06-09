package com.yangfeilong.live.user.index;

import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-20.
 */
public class UserIndexController extends BaseController{

    private UserIndexService userIndexService=new UserIndexService();

    //获得正在直播的用户列表
    public void isShowing(){
        renderJson(userIndexService.isShowing());
    }

    //获得有直播权限的用户列表
    public void beAbleToShow(){
        renderJson(userIndexService.beAbleToShow());
    }

    public void getNickName(){
        int id= Integer.parseInt(getPara("user_id"));
        renderJson(userIndexService.getNickNameById(id));
    }

}
