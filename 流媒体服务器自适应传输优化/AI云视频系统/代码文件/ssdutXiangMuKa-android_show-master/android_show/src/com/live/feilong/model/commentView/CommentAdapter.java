package com.live.feilong.model.commentView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.live.feilong.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gavin on 17-9-26.
 */

public class CommentAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater=null;

    public CommentAdapter(Activity activity, ArrayList<HashMap<String, Object>> data){
        this.activity=activity;
        this.data=data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.comments_list, null);
        TextView textView= (TextView) vi.findViewById(R.id.comments_model);
        HashMap<String, Object> item = new HashMap<String,Object>();
        Log.i("livetest","I am in step");
        item=data.get(position);
        // 设置ListView的相关值
        Object isPositive= item.get("mode").toString();
        if(isPositive.equals("1")){
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            textView.setLayoutParams(lp);
            textView.setTextColor(vi.getResources().getColor(R.color.blue_light));
        }else{
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
            textView.setLayoutParams(lp);
            textView.setTextColor(vi.getResources().getColor(R.color.red));
        }
        textView.setText(item.get("content").toString());
        return vi;
    }
}
