package com.live.feilong.activity.uploadvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.live.feilong.utils.MyActivityList;
import com.squareup.picasso.Picasso;
import com.live.feilong.common.net.CountingRequestBody;
import com.live.feilong.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainForge extends Activity {

    private ImageView turnBack;
    private TextView realseVideo;
    private ImageView chooseVideo;
    private ImageView chooseFace;
    private EditText videoName;
    private EditText videoDescription;
    private TextView textChooseVideo;
    private TextView textChooseFace;

    private File chooseFile=null;
    private String choosedFilePath=null;
    private String choosedFacePath=null;
    private String choosedFaceName=null;

    private String videoTitle=null;
    private String videoInfo=null;
    private Handler handler=null;
    private boolean isFinished=false;
    private RelativeLayout relativeLayout;
    private LinearLayout chooseUI;
    private TextView uploadClass;
    private TextView uploadProgress;
    private ProgressBar uploadProgressBar;
    private int videoDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainforge);
        MyActivityList.getInstance().addActivity(this);
        try{
            initData();
            initListener();
            initView();
            initThread();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void initData(){
        turnBack= (ImageView) findViewById(R.id.back_to_mainforge);
        realseVideo= (TextView) findViewById(R.id.release_video);
        chooseFace= (ImageView) findViewById(R.id.choose_face);
        chooseVideo= (ImageView) findViewById(R.id.choose_video);
        videoDescription= (EditText) findViewById(R.id.video_description);
        videoName= (EditText) findViewById(R.id.video_name);
        textChooseVideo= (TextView) findViewById(R.id.text_chooseVideo);
        textChooseFace= (TextView) findViewById(R.id.text_chooseFace);
        relativeLayout= (RelativeLayout) findViewById(R.id.isUploading);
        chooseUI= (LinearLayout) findViewById(R.id.chooseUI);
        uploadClass= (TextView) findViewById(R.id.uploadClass);
        uploadProgress= (TextView) findViewById(R.id.uploadProgress);
        uploadProgressBar= (ProgressBar) findViewById(R.id.uploadProgressBar);
        SharedPreferences chooseVideo=getSharedPreferences("chooseVideo", Context.MODE_PRIVATE);
        choosedFilePath=chooseVideo.getString("chooseVideoPath",null);
        videoDuration=chooseVideo.getInt("videoDruation",0);
    }
    public void initListener(){
        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
        chooseFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("crop",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent,2);
            }
        });

        realseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("user-info",Context.MODE_PRIVATE);
                if(!sharedPreferences.getBoolean("isOnLine",false)){
                    Toast.makeText(MainForge.this,"检测到您尚未登录,请先登录",Toast.LENGTH_LONG).show();
                }else{
                    UploadTask uploadTask=new UploadTask();
                    uploadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        turnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ClearSharedPreference();
            }
        });
    }

    public void initView(){
        try{
            chooseFile=new File(choosedFilePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(chooseFile!=null&&choosedFilePath!=null&&!choosedFilePath.equals("")){
            Picasso.with(this).load(R.drawable.ic_launcher).fit().centerCrop().into(chooseVideo);
            String[] array=choosedFilePath.split("/");
            textChooseVideo.setTextSize(14);
            textChooseVideo.setText(array[array.length-1]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initView();
    }


    //清除sharedpreferecnce里面的数据
    public void ClearSharedPreference(){
        SharedPreferences chooseVideo=getSharedPreferences("chooseVideo", Context.MODE_PRIVATE);
        chooseVideo.edit().putString("chooseVideoPath",null).commit();
        chooseVideo.edit().putInt("videoDruation",0).commit();
        chooseVideo.edit().putString("chooseVideoFace",null).commit();
    }

    //还原界面
    public void makeRecovery(){
        chooseFile=null;
        choosedFilePath=null;
        Picasso.with(this).load(R.drawable.add_video).fit().centerCrop().into(chooseVideo);
        textChooseVideo.setTextSize(18);
        textChooseVideo.setText("选择视频文件");

        choosedFacePath=null;
        Picasso.with(this).load(R.drawable.add_video).fit().centerCrop().into(chooseFace);
        textChooseFace.setTextSize(18);
        textChooseFace.setText("选择视频封面");

        videoDescription.setText("");
        videoName.setText("");

        uploadProgress.setText("当前进度:0%");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK){
            System.out.println("requestCode"+requestCode);
        }
        if(requestCode==1){
            try {
                //todo 获取图像路径，并且存储视频路径以及视频的时长
                Uri uri = data.getData();
                choosedFilePath = uri.getPath();
                SharedPreferences sharedPreferences=getSharedPreferences("chooseVideo",Context.MODE_PRIVATE);
                //todo 获取图像路劲
                choosedFacePath=sharedPreferences.getString("chooseFacePath",null);
                //todo 判断视频是否为有效视频
                if (choosedFilePath == null) {
                    Toast.makeText(this, "视频选择错误,请重试", Toast.LENGTH_LONG).show();
                } else {
                    //todo 二次判断
                    int time=0;
                    try{
                        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
                        mmr.setDataSource(choosedFilePath);
                        String duration=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        duration=duration.substring(0,duration.length()-3);
                        time=Integer.parseInt(duration);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainForge.this,"请选择视频文件",Toast.LENGTH_SHORT).show();
                        choosedFilePath=null;
                        return;
                    }
                    //todo 存储视频信息
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("chooseVideoPath",choosedFilePath).commit();
                    editor.putInt("videoDruation",time).commit(); //获得了视频的播放时长
                    initView();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(requestCode==2){
            try {
                //todo 获取视频信息，存储图片路径
                Uri uri = data.getData();
                choosedFacePath = uri.getPath();
                //todo 获取视频信息
                SharedPreferences sharedPreferences=getSharedPreferences("chooseVideo",Context.MODE_PRIVATE);
                choosedFilePath=sharedPreferences.getString("chooseVideoPath",null);
                videoDuration=sharedPreferences.getInt("videoDruation",0);
                //todo 判断图片是否符合要求
                if (choosedFacePath == null) {
                    Toast.makeText(this, "图片选择错误,请重试", Toast.LENGTH_LONG).show();
                } else {
                    try{
                        choosedFaceName = choosedFacePath.substring(choosedFacePath.lastIndexOf("/")+1);
                        textChooseFace.setText(choosedFaceName);
                        Picasso.with(this).load(new File(choosedFacePath)).fit().centerCrop().into(chooseFace);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainForge.this,"图片文件",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //存储图片
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("chooseFacePath",choosedFacePath).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据图片的url路径获得图片,但是考虑到通过设置bitmap的方式填充imageview不能够填充大规格的图片,所以这种方法放弃
     * 最终采用Picasso的方法填充图片
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url){
        FileInputStream fis=null;
        try{
            fis=new FileInputStream(url);
            Bitmap bitmap=BitmapFactory.decodeStream(fis);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //监听返回键,当返回的时候,清除sharedpreference缓存

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ClearSharedPreference();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        ClearSharedPreference();
        super.onDestroy();
    }

    public void initThread(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    NotHasFinished();
                }
                if(msg.what==1){
                    HasFinished();
                }
                if(msg.what==3){
                    Toast.makeText(MainForge.this,"上传失败",Toast.LENGTH_LONG).show();
                }
                if(msg.what==4){
                    relativeLayout.setVisibility(View.GONE);
                    chooseUI.setBackgroundColor(getResources().getColor(R.color.white));
                    chooseUI.setClickable(true);
                    Toast.makeText(MainForge.this,"上传成功",Toast.LENGTH_LONG).show();
                }
                if(msg.what==5){
                    int progress=(int)msg.arg1;
                    uploadProgressBar.setProgress(progress);
                    int a=msg.arg2;
                    uploadProgress.setText("当前进度:"+a+"%");
                }
                if(msg.what==6){ //表示文件已经上传完成
                    Log.i("test","完成了");
                    Toast.makeText(MainForge.this,"上传成功",Toast.LENGTH_LONG).show();
                    relativeLayout.setVisibility(View.GONE);
                    chooseUI.setBackgroundColor(getResources().getColor(R.color.white));
                    chooseUI.setClickable(true);
                }
                if(msg.what==7){//表示文件上传失败
                    Toast.makeText(MainForge.this,"上传失败",Toast.LENGTH_LONG).show();
                    relativeLayout.setVisibility(View.GONE);
                    chooseUI.setBackgroundColor(getResources().getColor(R.color.white));
                    chooseUI.setClickable(true);
                }
                if(msg.what==8){  //更新上传的进度条
                    Log.i("test","更新UI");
                    uploadProgress.setText("当前进度:"+msg.arg1+"%");
                }
                if(msg.what==9){ //正在压缩图片

                }
                super.handleMessage(msg);
            }
        };
        new MyThread().start();
    }

    //开启线程用来监听用户是否填完所有选项
    public class MyThread extends Thread{
        public void run(){
            while (!Thread.currentThread().isInterrupted()){
                videoTitle=videoName.getText().toString().trim();
                videoInfo=videoDescription.getText().toString();
                if(choosedFacePath!=null&&choosedFilePath!=null
                        &&videoInfo!=null&&videoTitle!=null
                        &&!videoInfo.equals("")&&!videoTitle.equals("")){
                    if(!isFinished){
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                        isFinished=true;
                    }

                }else{
                    if(isFinished){
                        Message message=new Message();
                        message.what=0;
                        isFinished=false;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void NotHasFinished(){
        realseVideo.setTextColor(getResources().getColor(R.color.gray));
        realseVideo.setClickable(false);
    }

    public void HasFinished(){
        realseVideo.setTextColor(getResources().getColor(R.color.white));
        realseVideo.setClickable(true);
    }

    //todo 使用异步处理的方法进行文件上传的操作
    private class UploadTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            relativeLayout.setVisibility(View.VISIBLE);
            chooseUI.setBackgroundColor(getResources().getColor(R.color.gray));
            chooseUI.setClickable(false);
        }

        @Override
        protected void onPostExecute(String s) {
            makeRecovery();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Message message=new Message();
            SharedPreferences sharedPreferences=getSharedPreferences("user-info",Context.MODE_PRIVATE);
            String token=sharedPreferences.getString("token",null);
            String url="http://112.74.182.83/liveshow/upload";
//            String url="http://192.168.69.14:8080/upload";
            OkHttpClient client=new OkHttpClient();
            String videoType=choosedFilePath.substring(choosedFilePath.lastIndexOf("."));
            RequestBody body=new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("token",token)
                    .addFormDataPart("video_name",videoTitle)
                    .addFormDataPart("description",videoInfo)
                    .addFormDataPart("duration",videoDuration+"")
                    .addFormDataPart("cover",choosedFaceName,RequestBody.create(MediaType.parse("image/png"),new File(choosedFacePath)))
                    .addFormDataPart("video",videoTitle+videoType,RequestBody.create(MediaType.parse("image/png"),new File(choosedFilePath)))
                    .build();
            /**
             * 重写继承RequestBody类，得到Listener接口
             */
            CountingRequestBody continueRequestBody=new CountingRequestBody(body, new CountingRequestBody.Listener() {
                @Override
                public void onRequestProgress(long byteWritten, long contentLength) {
                    int percent= (int) (byteWritten*100/contentLength);
                    onProgressUpdate(percent);
                }
            });
            final Request request=new Request.Builder().url(url).post(continueRequestBody).build();
            try {
                Response response=client.newCall(request).execute();
                String str=response.body().string();
                Log.i("livetest",str);
                HashMap<String,String> map= JSON.parseObject(str,new TypeReference<HashMap<String, String>>(){}.getType());
                if(map.get("state").equals("ok")){
                    message.what=6;
                    handler.sendMessage(message);
                }else{
                    message.what=7;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                ClearSharedPreference();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("test",values[0]+"%");
            Message message=new Message();
            message.what=8;
            message.arg1=values[0];
            handler.sendMessage(message);
        }
    }

}














