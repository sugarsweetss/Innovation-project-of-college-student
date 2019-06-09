package com.yangfeilong.live.common.service;

import com.yangfeilong.live.common.model.Comment;
import com.yangfeilong.live.common.model.TempComment;

/**
 * Created by gavin on 17-10-1.
 */
public interface TempCommentService {
    TempComment findById(Object id);
    boolean deleteById(Object id);
    boolean save(TempComment model);
    boolean update(TempComment model);
    boolean deleteByNickname(String nickName);
}
