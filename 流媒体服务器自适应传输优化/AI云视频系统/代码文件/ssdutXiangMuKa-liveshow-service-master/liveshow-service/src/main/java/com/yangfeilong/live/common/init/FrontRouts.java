package com.yangfeilong.live.common.init;

import com.jfinal.config.Routes;
import com.yangfeilong.live.index.IndexController;

/**
 * Created by gavin on 17-9-19.
 */
public class FrontRouts extends Routes{
    public void config() {
        setBaseViewPath("/WEB-INF");
        add("/", IndexController.class,"index");
    }
}
