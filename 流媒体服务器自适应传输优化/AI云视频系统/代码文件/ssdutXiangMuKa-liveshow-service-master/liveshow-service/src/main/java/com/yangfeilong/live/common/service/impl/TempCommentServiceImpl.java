package com.yangfeilong.live.common.service.impl;

import com.yangfeilong.live.common.model.Comment;
import com.yangfeilong.live.common.model.TempComment;
import com.yangfeilong.live.common.service.TempCommentService;

import java.util.List;

/**
 * Created by gavin on 17-10-1.
 */
public class TempCommentServiceImpl implements TempCommentService{
    public static final TempComment dao=new TempComment().dao();
    @Override
    public TempComment findById(Object id) {
        return dao.findById(id);
    }

    @Override
    public boolean deleteById(Object id) {
        return dao.deleteById(id);
    }

    @Override
    public boolean save(TempComment model) {
        try {
            model.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(TempComment model) {
        try{
            model.update();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByNickname(String nickName) {
        if(dao.deleteByNickName(nickName)){
            return true;
        }
        return false;
    }

    public List<TempComment> findByNickName(String nickName){
        nickName="'"+nickName+"'";
        return dao.find("select * from l_temp_comment where nick_name="+nickName);
    }


}



