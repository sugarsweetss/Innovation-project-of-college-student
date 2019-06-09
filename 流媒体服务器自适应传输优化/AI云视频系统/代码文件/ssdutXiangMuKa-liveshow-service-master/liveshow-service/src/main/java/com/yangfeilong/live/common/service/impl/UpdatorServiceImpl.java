package com.yangfeilong.live.common.service.impl;

import com.yangfeilong.live.common.model.Updator;
import com.yangfeilong.live.common.service.UpdatorService;

/**
 * Created by gavin on 17-10-3.
 */
public class UpdatorServiceImpl implements UpdatorService {
    private static final Updator dao=new Updator().dao();
    @Override
    public Updator findById(Object id) {
        return dao.findById(id);
    }

    @Override
    public boolean deleteById(Object id) {
        try{
            dao.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Updator model) {
        try{
            model.save();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Updator model) {
        try{
            model.update();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Updator findNewest(){
        Updator updator=dao.findFirst("select * from l_updator order by id desc");
        return updator;
    }
}


















