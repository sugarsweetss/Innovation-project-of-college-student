package com.yangfeilong.live.common.init;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisInterceptor;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.yangfeilong.live.common.model._MappingKit;

/**
 * Created by gavin on 17-9-19.
 */
public class Config extends JFinalConfig{

    public void configConstant(Constants constants) {
        //加载配置,随后可以用Prokit.get(...)获取值
        PropKit.use("sql_config");
        constants.setDevMode(true);
        constants.setBaseDownloadPath("/home/video/apk/");
        constants.setMaxPostSize(1024*1024*1024);
    }

    public void configRoute(Routes routes) {
        routes.add(new FrontRouts());
        routes.add(new AdminRouts());
    }

    //配置模板
    public void configEngine(Engine engine) {
        engine.setDevMode(true);//采用热加载,在模板改动时会及时生效
        engine.addSharedFunction("/WEB-INF/common/__layout.html");
    }

    /**
     * 配置插件
     * @param plugins
     */
    public void configPlugin(Plugins plugins) {
        //配置druid数据库连接池插件
        DruidPlugin druidPlugin=new DruidPlugin(PropKit.get("jdbcUrl"),PropKit.get("user"),PropKit.get("password").trim());
        plugins.add(druidPlugin);
        //配置ActiveRecord插件
        ActiveRecordPlugin arp=new ActiveRecordPlugin(druidPlugin);
        //所有映射在MappingKit中自动搞定
        _MappingKit.mapping(arp);
        plugins.add(arp);



        //配置Reids模块
        RedisPlugin tokenRedis=new RedisPlugin("token","localhost","201592009");
        plugins.add(tokenRedis);



    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {

    }

    public static DruidPlugin createDruidPlugin(){
        return new DruidPlugin(PropKit.get("jdbcUrl"),PropKit.get("user"),PropKit.get("password"));
    }

}











