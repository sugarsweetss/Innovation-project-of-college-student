package com.yangfeilong.live.user.register;

import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.controller.BaseController;


/**
 * Created by gavin on 17-9-28.
 */
public class RegisterController extends BaseController{
    RegisterService srv=new RegisterService();
    public void index(@Para("account")String account,@Para("nickname")String nickname,@Para("password")String password,@Para("validate")String validate){
        Cache cache= Redis.use();
        String v=cache.get(account);
        if(v.equals(validate)){
            renderJson(srv.registerUser(account,nickname,password));
        }else {
            renderJson(Ret.fail().set("error","验证码已过期"));
        }

    }
}
