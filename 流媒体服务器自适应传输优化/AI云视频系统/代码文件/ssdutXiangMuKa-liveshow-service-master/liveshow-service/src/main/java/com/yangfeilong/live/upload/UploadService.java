package com.yangfeilong.live.upload;

import com.jfinal.kit.FileKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.model.Video;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;
import com.yangfeilong.live.common.service.impl.VideoServiceImpl;

import java.io.File;
import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class UploadService {
    private UserServiceImpl userService=new UserServiceImpl();
    private VideoServiceImpl videoService=new VideoServiceImpl();

    public Ret upload(File videoFile,File coverFile,String videoName,String token,String description,long duration){
        Integer userId= Redis.use("token").get(token);
        final User user=userService.findById(userId);
        if(null==user){
            return Ret.fail("error","请通过客户端登录后上传文件");
        }
        if(user.getIdCard()==""||user.getIdCard()==null){
            //todo 在后期再这样实施
//            return Ret.fail("error","请先实名认证之后再上传文件");
        }


        String coverRootPath="/opt/apache-tomcat-8.5.20/webapps/androidSrc/localvideo/"+user.getNickName()+"/";
        String videoRootPath="/home/video/localvideo/"+user.getNickName()+"/";
        /**
         * 本地测试
         */
//        String coverRootPath="/home/gavin/localvideo/cover/"+user.getNickName()+"/";
//        String videoRootPath="/home/gavin/localvideo/video/"+user.getNickName()+"/";
        File rootCover=new File(coverRootPath);
        File rootVideo=new File(videoRootPath);
        if(!rootCover.exists()){
            rootCover.mkdirs();
        }
        if(!rootVideo.exists()){
            rootVideo.mkdirs();
        }

        List<Video> list=videoService.findVideosByName(videoName);
        String videoFileType= FileKit.getFileExtension(videoFile.getName());
        String coverFileType= FileKit.getFileExtension(coverFile.getName());
        if(!list.isEmpty()){
            videoName=videoName+"("+list.size()+")";
        }
        String coverFilePath=coverRootPath+videoName+"."+coverFileType;
        String videoFilePath=videoRootPath+videoName+"."+videoFileType;
        File finalCoverFile=new File(coverFilePath);
        File finalVideoFile=new File(videoFilePath);

        if(!coverFile.renameTo(finalCoverFile)||!videoFile.renameTo(finalVideoFile)){
            return Ret.fail("error","上传失败:无法上传文件");
        }

        user.setVideoAmount(user.getVideoAmount()+1);
        final Video video=new Video().setVideoName(videoName).setDescription(description).setUserId(userId)
                .setVideoFormat(videoFileType).setDuration(duration).setCover(videoName+"."+coverFileType);
        if (Db.tx(() -> userService.update(user) && videoService.save(video))) {
            return Ret.ok();
        }
        FileKit.delete(finalCoverFile);
        FileKit.delete(finalVideoFile);
        return Ret.fail("error","上传失败:数据库操作失败");
    }
}








