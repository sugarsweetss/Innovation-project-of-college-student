package com.yangfeilong.live.common.controller;

import com.jfinal.core.Controller;

/**
 * Created by gavin on 17-9-19.
 */

/**
 * 使用二次继承,方便对controller类操作
 */
public class BaseController extends Controller{
    public String getToken(){
        return getPara("token");
    }
}
