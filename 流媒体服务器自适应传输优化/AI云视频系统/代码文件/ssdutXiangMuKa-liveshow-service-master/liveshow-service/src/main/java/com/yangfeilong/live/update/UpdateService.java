package com.yangfeilong.live.update;

import com.yangfeilong.live.common.model.Updator;
import com.yangfeilong.live.common.service.impl.UpdatorServiceImpl;

/**
 * Created by gavin on 17-10-2.
 */
public class UpdateService {
    UpdatorServiceImpl updateService=new UpdatorServiceImpl();
    public Updator findNewest(){
        return updateService.findNewest();
    }
}
