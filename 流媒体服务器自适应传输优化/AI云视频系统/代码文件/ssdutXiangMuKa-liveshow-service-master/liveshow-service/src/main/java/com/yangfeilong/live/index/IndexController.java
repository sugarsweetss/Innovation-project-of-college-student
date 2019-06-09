package com.yangfeilong.live.index;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.redis.Redis;
import com.yangfeilong.live.common.service.impl.UserServiceImpl;

public class IndexController extends Controller {

    IndexService srv = new IndexService();
    private UserServiceImpl userService = new UserServiceImpl();

    public void index() {
        render("Login.html");
    }

    @ActionKey("/home")
    public void getHome() {

        //不应该写在这，应该用拦截器
        String token = getCookie("token");
        String nickName = userService.findById((Redis.use("token").get(token))).getNickName();
        String number = userService.findById((Redis.use("token").get(token))).getPhoneNumber();
        setAttr("nickName",nickName);
        setAttr("phoneNum",number);

        setAttr("all_data",srv.formHome(1));
        setAttr("hot_data",srv.formHome(2));
        //setAttr("index_data",srv.formHome(3));
        render("Home.html");
    }

    @ActionKey("/room")
    public void room() {

        //应该网页模板加拦截器，后期再改
        String token = getCookie("token");
        String nickName = userService.findById((Redis.use("token").get(token))).getNickName();
        String number = userService.findById((Redis.use("token").get(token))).getPhoneNumber();
        setAttr("nickName",nickName);
        setAttr("phoneNum",number);

        int video_id = getParaToInt("video_id");
        setAttr("data",srv.formRoom(video_id));

        render("Room.html");
    }

//    @ActionKey("/getimg")
//    public void getimg() {
//        String cover = getPara("cover");
//        String name = getPara("name");
//        renderFile("http://112.74.182.83/androidSrc/localvideo/" + name  + "/" + cover);
//    }

}
