package com.yangfeilong.live;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gavin on 17-10-1.
 */
public class test1 {
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        String str = "{\"commentList\":[{\"mode\":0,\"comment_time\":\"2017-10-01 20:57:58\",\"user_id\":1,\"id\":30,\"show_sec\":0.685,\"content\":\"hah\",\"video_id\":20},{\"mode\":1,\"comment_time\":\"2017-09-27 00:51:56\",\"user_id\":1,\"id\":26,\"show_sec\":4.0,\"content\":\"happy\",\"video_id\":20}],\"state\":\"ok\"}";
        CommentModel comments = JSON.parseObject(str, CommentModel.class);
        System.out.println(comments.getCommentList());
    }

    public static class CommentModel {
        String state;
        ArrayList<HashMap<String, Object>> commentList;

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


}