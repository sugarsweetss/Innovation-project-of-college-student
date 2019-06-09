package com.live.feilong.model.listView.baseModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gavin on 17-9-30.
 */

/**
 * 直播视频信息Model
 */
public class LiveVideoModel {
    private String state;
    List<HashMap<String,Object>> livevideolist;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<HashMap<String, Object>> getLivevideolist() {
        return livevideolist;
    }

    public void setLivevideolist(List<HashMap<String, Object>> livevideolist) {
        this.livevideolist = livevideolist;
    }
}
