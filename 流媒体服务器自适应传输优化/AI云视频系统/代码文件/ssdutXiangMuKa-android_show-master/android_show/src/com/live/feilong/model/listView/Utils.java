package com.live.feilong.model.listView;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by gavin on 17-9-6.
 */

//读取流的工具类
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try{
            byte[] bytes=new byte[buffer_size];
            for(;;){
                int count=is.read(bytes,0,buffer_size);
                if(count==-1){
                    is.close();
                    os.close();
                    break;
                }
                os.write(bytes,0,count);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
