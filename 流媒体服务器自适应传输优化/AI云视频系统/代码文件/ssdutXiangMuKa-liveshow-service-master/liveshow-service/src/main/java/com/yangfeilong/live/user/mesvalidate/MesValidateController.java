package com.yangfeilong.live.user.mesvalidate;

import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.controller.BaseController;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;
import com.yangfeilong.live.user.mesvalidate.util.MailSenderInfo;
import com.yangfeilong.live.user.mesvalidate.util.SimpleMailSender;

/**
 * Created by gavin on 17-9-28.
 */
public class MesValidateController extends BaseController{
    UserServiceImpl userService=new UserServiceImpl();
    public void index(@Para("mailbox")String mailbox){
        if(userService.checkMailExist(mailbox)){
            renderJson(Ret.fail().set("error","邮箱已存在"));
        }
        StringBuffer stringBuffer=new StringBuffer("");
        for(int i=0;i<4;++i){
            stringBuffer.append((int)(Math.random()*10));
        }
        String validate=stringBuffer.toString();
        MailSenderInfo mailSenderInfo=new MailSenderInfo();
        mailSenderInfo.setMailServerHost("smtp.qq.com");
        mailSenderInfo.setMailServerPort("587");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUsername("2296685742@qq.com");
        mailSenderInfo.setPassword("mhdgoknmlvckdjbi");
        mailSenderInfo.setFromAddress("2296685742@qq.com");
        mailSenderInfo.setToAddress(mailbox);
        mailSenderInfo.setContent("注册验证码:"+validate);
        mailSenderInfo.setSubject("欢迎注册洋葱直播平台账号");
        SimpleMailSender simpleMailSender = new SimpleMailSender();
        simpleMailSender.sendTextMail(mailSenderInfo);
        Cache cache= Redis.use();
        cache.set(mailbox,validate);
        cache.expire(mailbox,1800);
        renderJson(Ret.ok());
    }
}





















