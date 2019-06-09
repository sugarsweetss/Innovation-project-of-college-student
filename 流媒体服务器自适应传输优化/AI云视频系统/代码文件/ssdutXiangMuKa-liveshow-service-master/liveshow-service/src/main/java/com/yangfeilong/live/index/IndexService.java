package com.yangfeilong.live.index;

import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yangfeilong.live.common.service.impl.VideoServiceImpl;

import java.util.List;

public class IndexService {

    public Ret formHome(int order) {

        List<Record> vedioList;
        if (order == 1) {
            vedioList = Db.find("select nick_name,video_name,cover,comment_amounts,l_video.id,video_format from l_video join l_user on l_video.user_id = l_user.id order by upload_time DESC");
        } else {
            vedioList = Db.find("select nick_name,video_name,cover,comment_amounts,l_video.id,video_format from l_video join l_user on l_video.user_id = l_user.id order by comment_amounts DESC");
        }
        return Ret.ok("list",vedioList);
    }

    public Ret formRoom(int live_id) {
        Record vedioMsg = Db.findFirst("select nick_name,video_name,cover,comment_amounts,l_video.id,video_format from l_video join l_user on l_video.user_id = l_user.id where l_video.id = ?", live_id);
        return Ret.ok("msg",vedioMsg);
    }

}
