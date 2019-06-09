package com.yangfeilong.live.comments;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.yangfeilong.live.common.model.Comment;
import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.model.Video;
import com.yangfeilong.live.common.service.impl.CommentServiceImpl;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;
import com.yangfeilong.live.common.service.impl.VideoServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavin on 17-9-26.
 */
public class CommentService {
    VideoServiceImpl videoService=new VideoServiceImpl();
    UserServiceImpl userService=new UserServiceImpl();
    CommentServiceImpl commentService=new CommentServiceImpl();
    public Ret getComment(String nickName,String videoName){
        Video video=getVideo(nickName,videoName);
        if(video==null){
            return Ret.fail().set("error","视频或视频所属的用户不存在");
        }
        int video_id=video.getId();
        List<Comment> commentsList=commentService.findByVideoId(video_id);
        return Ret.ok().set("commentList",commentsList);
    }

    public Ret uploadComment(String nickName,String vidoeName,String context,String positive,String sec){
        Float second= Float.parseFloat(sec);
        User user=userService.findByNickName(nickName);
        if(user==null){
            return Ret.fail().set("error","视屏所属用户不存在");
        }
        Video video=getVideo(nickName,vidoeName);
        if(video==null){
            return Ret.fail().set("error","视屏不存在");
        }
        int videoId=video.getId();
        Comment comment=new Comment()
                .setContent(context)
                .setMode(positive.equals("true")?1:0)
                .setShowSec(second)
                .setUserId(user.getId())
                .setVideoId(videoId);
        video.setCommentAmounts(video.getCommentAmounts()+1);
        if(Db.tx(()->commentService.save(comment)&&videoService.update(video))){
            return Ret.ok();
        }
        return Ret.fail().set("error","数据库读写失败");
    }

    public Video getVideo(String nickName,String videoName){
        User user=userService.findByNickName(nickName);
        if(user==null){
            return null;
        }
        int id=user.getId();
        Video video=videoService.findVideoByUIDandName(id,videoName);
        return video;
    }
}






























