package com.yangfeilong.live.temp_commment;

import com.jfinal.core.paragetter.Para;
import com.yangfeilong.live.common.controller.BaseController;

/**
 * Created by gavin on 17-10-1.
 */
public class TempCommentController extends BaseController{
    TempCommentService srv=new TempCommentService();
    public void uploadTempComments(@Para("nickName")String nickName, @Para("comment")String commment,
                                   @Para("positive")String positive){
        renderJson( srv.uploadTempComment(nickName,commment,positive.equals("true")?true:false));
    }

    public void deleteTempComments(@Para("nickName")String nickName){
        renderJson(srv.deleteTempComment(nickName));
    }

    public void getTempComments(@Para("nickName")String nickName){
        renderJson(srv.getTempComment(nickName));
    }
}
