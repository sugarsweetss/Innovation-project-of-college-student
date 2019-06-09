package com.live.feilong.model.listView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.live.feilong.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; //用来下载图片的类，后面有介绍

    public LazyAdapter(Activity a, ArrayList<HashMap<String, Object>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.localvideo_list, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // 标题
        TextView artist = (TextView)vi.findViewById(R.id.artist); // 歌手名
        TextView duration = (TextView)vi.findViewById(R.id.duration); // 时长
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // 缩略图
        TextView update_time = (TextView) vi.findViewById(R.id.update_time); //上传时间
        TextView comments_amounts= (TextView) vi.findViewById(R.id.comments_amounts); //评论量

        HashMap<String, Object> item = new HashMap<String, Object>();
         item= data.get(position);

        // 设置ListView的相关值
        title.setText(item.get("ItemName").toString());
        artist.setText(item.get("ItemUser").toString());
        duration.setText(item.get("ItemShiChang").toString());
        update_time.setText(item.get("ItemTime").toString());
        comments_amounts.setText(item.get("Item_comments_amounts").toString());
        Log.i("livetestfeilong","imageLoader");
        imageLoader.DisplayImage(item.get("ItemImage").toString(), thumb_image);
        return vi;
    }
}