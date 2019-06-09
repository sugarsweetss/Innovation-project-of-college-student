package com.live.feilong.model.listView;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by gavin on 17-9-6.
 */

public class FileCache {
    private File cacheDir;
    public FileCache(Context context) {
        //找一个用来缓存图片的路径
        cacheDir=new File(Environment.getExternalStorageDirectory()+"/Adobe直播/图片缓存/");
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f=new File(cacheDir,filename);
        return f;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files){
            f.delete();
        }
    }
}
