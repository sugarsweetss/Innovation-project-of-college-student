package com.yangfeilong.live.common.service;

import com.yangfeilong.live.common.model.User;

import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public interface UserService {
    User findById(Object id);
    boolean deleteById(Object id);
    boolean save(User model);
    boolean update(User model);
    List<User> findByState(int state);
    User findByAccount(String account);
    List<User> findHasIdCard();
    boolean login(String account, String password);
}
