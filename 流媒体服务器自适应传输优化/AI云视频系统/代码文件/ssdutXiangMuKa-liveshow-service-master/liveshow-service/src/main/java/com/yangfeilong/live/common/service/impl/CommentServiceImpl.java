package com.yangfeilong.live.common.service.impl;

import com.yangfeilong.live.common.model.Comment;
import com.yangfeilong.live.common.service.CommentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class CommentServiceImpl implements CommentService{
    private static final Comment dao=new Comment().dao();
    public Comment findById(Object id) {
        return dao.findById(id);
    }

    public List<Comment> findByVideoId(int id){
        String query="select * from l_comment where video_id="+id+" order by show_sec";
        return dao.find(query);
    }

    public boolean deleteById(Object id) {
        try{
            dao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save(Comment model) {
        try{
            model.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Comment model) {
        try{
            model.update();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
