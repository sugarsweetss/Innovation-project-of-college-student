package com.yangfeilong.live.common.service.impl;

import com.yangfeilong.live.common.model.User;
import com.yangfeilong.live.common.service.UserService;

import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class UserServiceImpl implements UserService{
    private static final User dao=new User().dao();

    public User findById(Object id) {
        return dao.findById(id);
    }

    public User findByNickName(String nickName){
        if(nickName!=null){
            nickName="'"+nickName+"'";
        }
        String query="select * from l_user where nick_name="+nickName;
        List<User> list=dao.find(query);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public boolean deleteById(Object id) {
        return dao.deleteById(id);
    }

    public boolean save(User model) {
        try{
            model.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(User model) {
        try{
            model.update();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<User> findByState(int state) {
        return dao.find("select * from l_user where state = "+state);
    }

    public User findByAccount(String account) {
        account="'"+account+"'";
        List<User> list=dao.find("select * from l_user where account ="+account);
        if(list.isEmpty()||list==null){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<User> findHasIdCard() {
        return dao.find("select * from l_user where id_card is not null");
    }

    public boolean login(String account, String password) {
        account="'"+account+"'";
        password="'"+password+"'";
        List<User> list=dao.find("select * from l_user where account = "+account+" and password = "+password);
        if(list==null||list.isEmpty()){
            return false;
        }else{
            User user=list.get(0);
            return true;
        }
    }

    public boolean checkMailExist(String mailbox){
        mailbox="'"+mailbox+"'";
        List<User> list=dao.find("select * from l_user where account="+mailbox);
        if(list.isEmpty()){
            return true;
        }
        return false;
    }

    public List<User> findLiveUser(){
        List<User> list=dao.find("select nick_name from l_user where state=2");
        return list;
    }


}






















