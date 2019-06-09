package com.yangfeilong.live.delete;

import com.jfinal.kit.FileKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.model.Video;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;
import com.yangfeilong.live.common.service.impl.VideoServiceImpl;

import java.io.File;

/**
 * Created by gavin on 17-9-25.
 */
public class DeleteService {
    VideoServiceImpl videoService=new VideoServiceImpl();
    UserServiceImpl userService=new UserServiceImpl();
    public Ret deleteVideo(int videId,String token){
        Integer userId= Redis.use("token").get(token);
        User user=userService.findById(userId);
        if(user==null){
            return Ret.fail().set("error","请先在客户端登录");
        }else if(user.getLevel()==0){
            return Ret.fail().set("errot","请以管理员的身份登录");
        }

        Video video=videoService.findVideoById(videId);
        if(video==null){
            return Ret.fail().set("error","视频不存在");
        }
        User kUser=userService.findById(video.getUserId());
      String coverFilePath="/opt/apache-tomcat-8.5.20/webapps/androidSrc/localvideo/"+kUser.getNickName()+"/"+video.getCover();
      String videoFilePath="/home/video/localvideo/"+kUser.getNickName()+"/"+video.getVideoName()+"."+video.getVideoFormat();
        /**
         * 本地测试
         */
//        String coverFilePath="/home/gavin/localvideo/cover/"+kUser.getIdCard()+"/"+video.getCover();
//        String videoFilePath="/home/gavin/localvideo/video/"+kUser.getIdCard()+"/"+video.getVideoName()+"."+video.getVideoFormat();
        System.out.println(coverFilePath+":"+videoFilePath);
        File coverFile=new File(coverFilePath);
        File videoFile=new File(videoFilePath);
        kUser.setVideoAmount(kUser.getVideoAmount()-1);
        if(Db.tx(()->userService.update(kUser)&&videoService.deleteVideoById(videId))){
            FileKit.delete(coverFile);
            FileKit.delete(videoFile);
            return Ret.ok().set("successful","删除成功");
        }
        return Ret.fail().set("error","删除失败");
    }
}

























