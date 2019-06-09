package com.live.feilong.activity.uploadvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.live.feilong.R;
import com.live.feilong.utils.MyActivityList;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 发布视屏
 */
public class ChooseVideo extends Activity {
//    private File[] fileLists=null;
    private List<File> fileLists=new ArrayList<File>();
    private String BasePath="/sdcard/Adobe直播/录制/";
    private ListView listView;
    private ArrayList<HashMap<String,String>> listItem=null;
    private List<String> listName=new ArrayList<String>();
    private List<RadioButton> radioButtonList=new ArrayList<RadioButton>();
    private int chooseId=-1;
    private TextView makeSure;
    private ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_video);
        MyActivityList.getInstance().addActivity(this);
        initData();
        initView();
        initListener();
    }
    public void initData(){
        //得到录制文件夹下的所有文件
        File[] mainFiles=new File(BasePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.isDirectory()){
                    return true;
                }else{
                    return false;
                }
            }
        });
        for(File f : mainFiles){
            String name=f.getPath().replace(BasePath,"");
            String videoPath=f.getPath()+"/视频/"+name+".mp4";
            File video=new File(videoPath);
            if(video.exists()){
                fileLists.add(video);
                listName.add(name);
            }
        }


    }
    public void initView(){
        listItem=new ArrayList<>();
        Log.i("listItem刚刚初始化的大小",""+listItem.size());
        listView= (ListView) findViewById(R.id.choose_list);
        makeSure= (TextView) findViewById(R.id.make_sure);
        goBack= (ImageView) findViewById(R.id.go_back);
        for(int i=0;i<listName.size();++i){
            HashMap<String,String> map=new HashMap<String,String>();
            File file=fileLists.get(i);
            map.put("ItemName",listName.get(i));
            map.put("ItemUser","路径:"+file.getPath());
            map.put("ItemTime","创建时间:"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(file.lastModified())));
            map.put("ItemShiChang","大小:"+FormetFileSize(file.length()));
            map.put("Item_comments_amounts","");
            listItem.add(map);
        }
        listView.setAdapter(new MyLazyAdapter(this,listItem));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                Uri uri=Uri.parse("file://"+fileLists.get(position).getPath());
                intent.setDataAndType(uri,"video/mp4");
                startActivity(intent);

            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }



    /*
    自定义适配器
     */
    public class MyLazyAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private LayoutInflater inflater=null;

        public MyLazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.localvideo_list, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // 标题
            TextView artist = (TextView)vi.findViewById(R.id.artist); // 歌手名
            TextView duration = (TextView)vi.findViewById(R.id.duration); // 时长
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // 缩略图
            TextView update_time = (TextView) vi.findViewById(R.id.update_time); //上传时间
            TextView comments_amounts= (TextView) vi.findViewById(R.id.comments_amounts); //评论量
            RadioButton radioButton= (RadioButton) vi.findViewById(R.id.radioButtonChooseVideo);
            boolean isSame=false;
            for(RadioButton r:radioButtonList){
                if(r==radioButton||r==null){
                    isSame=true;
                }
            }
            if(!isSame){
                radioButtonList.add(radioButton);
            }
            //利用所有监听设置都是在view实现完成之后触发这个特性
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=0;
                    for(RadioButton r:radioButtonList){
                        if(pos==position){
                            makeSure.setTextColor(getResources().getColor(R.color.white));
                            makeSure.setClickable(true);
                            chooseId=position;
                            r.setChecked(true);
                        }else{
                            r.setChecked(false);
                        }
                        pos++;
                    }
                }
            });
            HashMap<String, String> item = new HashMap<String, String>();
            item= data.get(position);

            // 设置ListView的相关值
            title.setText(item.get("ItemName").toString());
            artist.setText(item.get("ItemUser").toString());
            duration.setText(item.get("ItemShiChang").toString());
            update_time.setText(item.get("ItemTime").toString());
            comments_amounts.setText(item.get("Item_comments_amounts").toString());
            thumb_image.setImageResource(R.drawable.video);
            return vi;
        }
    }

    public String FormetFileSize(long fileS)
    {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public void initListener(){
        makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseVideo.this,MainForge.class);
                intent.putExtra("theChoose",fileLists.get(chooseId).getPath());
                MediaMetadataRetriever mmr=new MediaMetadataRetriever();
                mmr.setDataSource(fileLists.get(chooseId).getPath());
                String duration=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                duration=duration.substring(0,duration.length()-3);
                Long time=Long.parseLong(duration);
                SharedPreferences chooseVideo=getSharedPreferences("chooseVideo",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=chooseVideo.edit();
                editor.putString("chooseVideoPath",fileLists.get(chooseId).getPath()).commit();
                editor.putLong("videoDruation", time).commit(); //获得了视频的播放时长
                Log.i("test","视频的时长为:"+time);
                startActivity(intent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseVideo.this,MainForge.class);
                startActivity(intent);
            }
        });
    }


}










