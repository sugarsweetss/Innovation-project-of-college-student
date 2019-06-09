package com.yangfeilong.live.common.service;

import com.yangfeilong.live.common.model.Comment;

/**
 * Created by gavin on 17-9-20.
 */
public interface CommentService {
    Comment findById(Object id);
    boolean deleteById(Object id);
    boolean save(Comment model);
    boolean update(Comment model);

}
