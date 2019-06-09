package com.yangfeilong.live.user.login;

import com.jfinal.core.ActionKey;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-20.
 */
public class LoginController extends BaseController{
    private LoginService loginService=new LoginService();
    public void index(@Para("account")String account,@Para("password")String password){
        Ret ret=loginService.login(account,password);
        System.out.println(ret);
        renderJson(ret);
    }

    @ActionKey("/logout")
    public void logout(){
        String token=getToken();
        Ret ret=loginService.logout(token);
        renderJson(ret);
    }
}









