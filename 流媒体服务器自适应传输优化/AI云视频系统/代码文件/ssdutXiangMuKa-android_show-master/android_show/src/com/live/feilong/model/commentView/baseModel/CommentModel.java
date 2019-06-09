package com.live.feilong.model.commentView.baseModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by gavin on 17-9-26.
 */

public class CommentModel {
    String state;
    ArrayList<HashMap<String,Object>>commentList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<HashMap<String, Object>> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<HashMap<String, Object>> commentList) {
        this.commentList = commentList;
    }
}
