package com.yangfeilong.live.comments;

import com.jfinal.core.paragetter.Para;
import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-9-26.
 */
public class CommentController extends BaseController{
    private CommentService srv=new CommentService();
    public void getComments(@Para("nickName")String nickName,@Para("videoName")String videoName){
        renderJson(srv.getComment(nickName,videoName));
    }

    public void uploadComments(@Para("nickName")String nickName,@Para("videoName")String videoName,
                               @Para("comment")String comment,@Para("positive")String positive,@Para("sec")String second){
        renderJson(srv.uploadComment(nickName,videoName,comment,positive,second));
    }


}
