package com.yangfeilong.live.user.register;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;

/**
 * Created by gavin on 17-9-28.
 */
public class RegisterService {
    UserServiceImpl userService=new UserServiceImpl();
    public Ret registerUser(String account, String nickname, String password){

        User user=new User()
                .setAccount(account)
                .setNickName(nickname)
                .setPwd(password)
                .setSalt(HashKit.md5(password));
        userService.save(user);
        return Ret.ok();
    }
}
