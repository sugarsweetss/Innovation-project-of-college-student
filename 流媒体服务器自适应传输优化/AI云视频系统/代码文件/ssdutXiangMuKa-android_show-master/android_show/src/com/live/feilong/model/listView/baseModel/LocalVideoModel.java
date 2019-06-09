package com.live.feilong.model.listView.baseModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */

/**
 * 本地视频信息model
 */
public class LocalVideoModel {
    private String state;
    List<HashMap<String,Object>> localvideolist;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<HashMap<String, Object>> getLocalvideolist() {
        return localvideolist;
    }

    public void setLocalvideolist(List<HashMap<String, Object>> localvideolist) {
        this.localvideolist = localvideolist;
    }
}
