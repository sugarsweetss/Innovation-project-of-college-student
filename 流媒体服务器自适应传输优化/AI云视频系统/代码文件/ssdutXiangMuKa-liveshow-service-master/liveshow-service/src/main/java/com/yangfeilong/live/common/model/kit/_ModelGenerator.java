package com.yangfeilong.live.common.model.kit;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;
import com.yangfeilong.live.common.init.Config;
import org.apache.log4j.BasicConfigurator;

import javax.sql.DataSource;

/**
 * Created by gavin on 17-9-20.
 */
public class _ModelGenerator {
    public static void main(String[] args){
        //baseModel使用的包名
        String baseModelPackageName="com.yangfeilong.live.common.model.base";
        //base model 文件的保存路径
        String baseModelOutputDir= PathKit.getWebRootPath()+"/src/main/java/com/yangfeilong/live/common/model/base";
        //model 所使用的包名(MappingKit 默认使用的包名)
        String modelPackageName="com.yangfeilong.live.common.model";
        //model文件保存路径
        String modelOutputDir=baseModelOutputDir+"/..";

        //创建生成器
        Generator generator=new Generator(getDataSource(),baseModelPackageName,baseModelOutputDir,modelPackageName,modelOutputDir);
        generator.setGenerateChainSetter(true);
        generator.setGenerateDaoInModel(true);
        generator.setGenerateDataDictionary(false);
        generator.setRemovedTableNamePrefixes("l_");
        generator.generate();

    }

    public static DataSource getDataSource(){
        PropKit.use("sql_config");
        DruidPlugin druidPlugin= Config.createDruidPlugin();
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }
}
