package com.yangfeilong.live.update;

import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.yangfeilong.live.common.controller.BaseController;
import com.yangfeilong.live.common.model.Updator;

import java.io.File;

/**
 * Created by gavin on 17-10-2.
 */
public class UpdateController extends BaseController{
    UpdateService srv=new UpdateService();

    /**
     * 检查是否有更新
     */
    public void index(@Para("version")String version){
        Updator updator=srv.findNewest();
        if(version.trim().equals(updator.getVersion())){
            renderJson(Ret.ok().set("update","No"));
        }else{
            Ret ret=new Ret().setOk();
            String updateUrl="http://112.74.182.83/liveshow/update/download?version=";
            ret.set("update","Yes")
                    .set("new_version",updator.getVersion())
                    .set("apk_file_url",updateUrl+updator.getVersion())
                    .set("update_log",updator.getLog())
                    .set("constraint",updator.getConstraintion())
                    .set("new_md5","201592009")
                    .set("target_size",updator.getSize());
            renderJson(ret);
        }
    }

    public void download(@Para("version")String version){
        System.out.println("I am here");
        try{
            renderFile("Live"+version+".apk");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}






















