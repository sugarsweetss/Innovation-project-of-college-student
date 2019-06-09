package com.yangfeilong.live.common.service;

import com.yangfeilong.live.common.model.Video;

import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public interface VideoService {
    Video findVideoById(Object id);
    boolean deleteVideoById(Object id);
    boolean save(Video model);
    boolean update(Video model);
    List<Video> getLocalVideoList();
}
