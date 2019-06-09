package com.yangfeilong.live.user.login;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;

/**
 * Created by gavin on 17-9-20.
 */
public class LoginService {
    private UserServiceImpl userService=new UserServiceImpl();
    public Ret login(String account,String password){
        User user=userService.findByAccount(account);
        if(null==user){
            return Ret.fail("wrong","用户名不存在");
        }
        if(user.getState()!=0){
//            return Ret.fail().set("error","账户已在另一处登录");
            //todo 处理一个账号在多地点登录
        }
        String salt=user.getSalt();
        if(!HashKit.md5(password).equals(salt)){
            return Ret.fail("wrong","密码错误");
        }
        String token= StrKit.getRandomUUID();
        Cache tokenCache= Redis.use("token");
        tokenCache.set(token,user.getId());
        user.setState(1);
        userService.update(user);
        return Ret.ok().set("token",token).set("nickName",user.getNickName()).set("mailbox",user.getAccount());
    }
    public Ret logout(String token){
        try {
            Cache cache=Redis.use();
            User user=userService.findById(cache.get(token));
            user.setState(0);
            userService.update(user);
            Redis.use("token").del(token);
        }catch (Exception e) {
            e.printStackTrace();
           return Ret.fail().set("error","未登录");
        }
        return Ret.ok();
    }
}
