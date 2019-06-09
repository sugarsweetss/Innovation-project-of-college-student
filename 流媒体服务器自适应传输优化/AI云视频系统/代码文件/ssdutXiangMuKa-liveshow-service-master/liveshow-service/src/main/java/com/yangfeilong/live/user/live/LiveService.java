package com.yangfeilong.live.user.live;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;


/**
 * Created by gavin on 17-9-29.
 */
public class LiveService {
    UserServiceImpl userService=new UserServiceImpl();
    public Ret liveon(String token){
        Cache cache= Redis.use("token");
        User user=null;
        try{
            user=userService.findById(cache.get(token));
        }catch (Exception e){
            e.printStackTrace();
            return Ret.fail().set("error","未登录");
        }
        if(user==null){
            return Ret.fail().set("error","未登录");
        }
        if(user.getState()==0){
            return Ret.fail().set("error","未登录");
        }
        if(user.getState()==2){
            return Ret.fail().set("error","正在直播");
        }
        user.setState(2);
        userService.update(user);
        return Ret.ok();
    }

    public Ret liveoff(String token){
        Cache cache=Redis.use("token");
        User user=null;
        try{
            user=userService.findById(cache.get(token));
        }catch (Exception e){
            e.printStackTrace();
            return Ret.fail().set("error","未登录");
        }
        if(user==null){
            return Ret.fail().set("error","未登录");
        }
        if(user.getState()==0){
            return Ret.fail().set("error","未登录");
        }
        if(user.getState()==1){
            return Ret.fail().set("error","未直播");
        }
        user.setState(1);
        userService.update(user);
        return Ret.ok();
    }

}



















