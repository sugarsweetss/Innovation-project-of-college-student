package com.yangfeilong.live.temp_commment;

import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.model.TempComment;
import com.yangfeilong.live.common.service.impl.TempCommentServiceImpl;

import java.util.List;

/**
 * Created by gavin on 17-10-1.
 */
public class TempCommentService {
    TempCommentServiceImpl tempCommentService=new TempCommentServiceImpl();
    public Ret uploadTempComment(String nickName, String comment, boolean ispositive){
        TempComment tempComment=new TempComment()
                .setContent(comment)
                .setMode(ispositive?1:0)
                .setNickName(nickName);
        if(tempCommentService.save(tempComment)){
            return Ret.ok();
        }
        return Ret.fail();
    }


    public Ret deleteTempComment(String nickName){
        if(tempCommentService.deleteByNickname(nickName)){
            return Ret.ok();
        }
        return Ret.fail();
    }

    public Ret getTempComment(String nickName){
        List<TempComment> list=tempCommentService.findByNickName(nickName);
        return Ret.ok().set("commentList",list);
    }
}
