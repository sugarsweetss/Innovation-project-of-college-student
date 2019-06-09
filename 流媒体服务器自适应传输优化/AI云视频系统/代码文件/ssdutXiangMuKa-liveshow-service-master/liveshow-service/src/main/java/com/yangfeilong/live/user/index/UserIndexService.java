package com.yangfeilong.live.user.index;

import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.service.UserService;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;

import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class UserIndexService {
    private UserServiceImpl userService=new UserServiceImpl();

    public Ret isShowing(){
        return null;
    }

    public Ret beAbleToShow(){
        List<User> users=userService.findHasIdCard();
        return Ret.ok("userlist",users);
    }

    public Ret getNickNameById(int id){
        User user=userService.findById(id);
        return Ret.ok("nickname",user.getNickName());
    }
}
