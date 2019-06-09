
package com.live.feilong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ksyun.media.streamer.demo.DemoActivity;
import com.live.feilong.R;
import com.live.feilong.activity.personal.LogIn;
import com.live.feilong.activity.uploadvideo.MainForge;
import com.live.feilong.fragment.LiveFragment;
import com.live.feilong.fragment.ShouYe;
import com.live.feilong.utils.FeilongHelper;
import com.live.feilong.utils.MyActivityList;
import com.nicevideoplayer.NiceVideoPlayerManager;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SocketHandler;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;
    private TextView headerTitle=null;
    private RadioGroup group;

    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;

    private FrameLayout main_container;

    private GestureDetector gestureDetector;
    private LinearLayout leftLinearlayout;

    private ImageView showPage;

    private boolean isShowingPersonal=false;//用来判断是否正在显示个人界面
    private LinearLayout displayView;

    private TextView landOnText;
    private Button logout;

    private ImageView uploadVideo;
    private boolean isOnLine=false;//用来表示是否为登录状态 true:登录 false:离线
    private RadioButton homeButton;
    private RadioButton liveButton;
    private ImageView personalHead;   //个人首页的头像
    private TextView accountMailTextview;
    private Button checkForNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivityList.getInstance().addActivity(this);
        Log.i("feilongtest","MainActivity start...");
        headerTitle= (TextView) findViewById(R.id.textHeadTitle);
        fragmentManager = getSupportFragmentManager();
        a();
        security_lock();
        initData(savedInstanceState);
        initView();
        initListener();
    }

    private void security_lock(){
        String url = "http://112.74.182.83/liveshow/lock";
        String result = new FeilongHelper().getURL(url);
        Log.i("longge", result);
        if(result.equals("no")){{
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在联网的情况下使用")
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyActivityList.getInstance().exit();
                        }
                    }).create().show();
        }}else if(!(result.equals("mengchuang"))){
            Log.i("longge","执行了");
            MyActivityList.getInstance().exit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshView();//检测登录状态
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
    }

    private void initData(Bundle savedInstanceState) {
        fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "ImFragment","ActivityMode", "MemberFragment"));
        currIndex = 0;
        if(savedInstanceState != null) {
            currIndex = savedInstanceState.getInt(CURR_INDEX);
            hideSavedFragment();
        }
    }

    private void hideSavedFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if(fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    private void initView() {
        checkForNew= (Button) findViewById(R.id.check_newer);
        accountMailTextview= (TextView) findViewById(R.id.account_mail_textview);
        homeButton= (RadioButton) findViewById(R.id.foot_bar_home);
        liveButton= (RadioButton) findViewById(R.id.foot_bar_im);
        uploadVideo= (ImageView) findViewById(R.id.upload_video);
        logout= (Button) findViewById(R.id.log_out);
        landOnText= (TextView) findViewById(R.id.needToLandon);
        leftLinearlayout= (LinearLayout) findViewById(R.id.left_linearlayout);
        personalHead= (ImageView) findViewById(R.id.person_info);
        gestureDetector=new GestureDetector(this,new MyOnGestureListener());
        main_container= (FrameLayout) findViewById(R.id.fragment_container);
        group = (RadioGroup) findViewById(R.id.group);
        showPage= (ImageView) findViewById(R.id.personal_head);
        displayView= (LinearLayout) findViewById(R.id.dispaly_view);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home: currIndex = 0; headerTitle.setText("所有视频"); break;
                    case R.id.foot_bar_im: currIndex = 1; headerTitle.setText("正在直播"); break;
                    case R.id.live_show:caseToLive();break;
//                    case R.id.foot_bar_activity: currIndex = 2; headerTitle.setText("动态"); break;
//                    case R.id.main_footbar_user: currIndex = 3; headerTitle.setText("个人"); break;
                    default: break;
                }
                showFragment();
            }
        });
        showFragment();
    }

    public void caseToLive(){
        if(currIndex==0){
            homeButton.setChecked(true);
        }else{
            liveButton.setChecked(true);
        }
        if(!isOnLine){
            Toast.makeText(MainActivity.this,"请先登录，然后才能使用直播功能呦",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(MainActivity.this, DemoActivity.class);
        startActivity(intent);
    }

    private void showFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if(fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if(f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:return new ShouYe();
            case 1:return new LiveFragment();
            case 2:return new LiveFragment();
            case 3:return new LiveFragment();
            default: return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isShowingPersonal){
            leftLinearlayout.setVisibility(View.GONE);
            isShowingPersonal=false;
            return;
        }
        if (NiceVideoPlayerManager.instance().onBackPressd())
            return;
        moveTaskToBack(true);
        super.onBackPressed();
    }

    public void initListener(){
        main_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


        showPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowingPersonal){
                    leftLinearlayout.setVisibility(View.VISIBLE);
                    isShowingPersonal=true;
                }
            }
        });

        personalHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOnLine){
                    SharedPreferences userInfo=getSharedPreferences("user-info", Context.MODE_PRIVATE);
                    String username=userInfo.getString("username",null);
                    if(username==null){
                        Intent intent=new Intent(MainActivity.this,LogIn.class);
                        startActivityForResult(intent,0);
                    }
                }
            }
        });

        displayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftLinearlayout.setVisibility(View.GONE);
                isShowingPersonal=false;
            }
        });

        /**
         * 退出登录按钮设置监听
         */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnLine){
                    SharedPreferences sharedPreferences=getSharedPreferences("user-info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    String token=sharedPreferences.getString("token",null);
                    OkHttpClient client=new OkHttpClient();
                    RequestBody formBody=new FormBody.Builder().add("token",token).build();
                    Request request=new Request.Builder().url("http://112.74.182.83/liveshow/logout").post(formBody).build();
                    try {
                        Response response=client.newCall(request).execute();
                        String result=response.body().string();
                        HashMap<String,String> map= JSON.parseObject(result,new TypeReference<HashMap<String, String>>(){}.getType());
                        String rs=map.get("state");
                        if(rs.equals("ok")){
                            Toast.makeText(MainActivity.this,"退出成功",Toast.LENGTH_LONG).show();
                            editor.putString("user_id",null);
                            editor.putString("token",null);
                            editor.putBoolean("isOnLine",false);
                            editor.commit();
                            isOnLine=false;
                            refreshView();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"检测到你尚未登录",Toast.LENGTH_LONG).show();
                }
            }
        });



        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MainForge.class);
                startActivity(intent);
            }
        });

        checkForNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="http://112.74.182.83/liveshow/update?appKey=201592009&version=5.0";
                String str=new FeilongHelper().postURL(url,new HashMap<String, String>());
                if(str.equals("{\"update\":\"No\",\"state\":\"ok\"}")){
                    Toast.makeText(MainActivity.this,"已经是最新版",Toast.LENGTH_LONG).show();
                    return;
                }
                try{
                    new UpdateAppManager
                            .Builder()
                            //当前Activity
                            .setActivity(MainActivity.this)
                            //更新地址
                            .setUpdateUrl(url)
                            //实现httpManager接口的对象
                            .setHttpManager(new UpdateAppHttpUtil())
                            .build()
                            .update();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0://登录成功之后
                landOnText.setText("已登录");
                break;
            default:
                break;
        }
    }

    public void refreshView(){
        SharedPreferences sharedPreferences=getSharedPreferences("user-info",Context.MODE_PRIVATE);
        isOnLine= sharedPreferences.getBoolean("isOnLine",false);
        if(isOnLine){
            String nickName=sharedPreferences.getString("nickName","");
            String mailbox=sharedPreferences.getString("mailbox","");
            landOnText.setText(nickName);
            accountMailTextview.setText("邮箱:"+mailbox);
            accountMailTextview.setBackgroundResource(R.drawable.live_btn_blue);
            logout.setBackgroundResource(R.drawable.live_btn_blue);
            personalHead.setBackgroundResource(R.drawable.live_on_line);
        }else{
            landOnText.setText("尚未登录");
            accountMailTextview.setText("邮箱:");
            accountMailTextview.setBackgroundResource(R.drawable.live_btn_gray);
            logout.setBackgroundResource(R.drawable.live_btn_gray);
            personalHead.setBackgroundResource(R.drawable.live_off_line);
        }
    }
    public void a() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    }

    class UpdateAppHttpUtil implements HttpManager {
        /**
         * 异步get
         *
         * @param url      get请求地址
         * @param params   get参数
         * @param callBack 回调
         */
        @Override
        public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkHttpUtils.get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callBack.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            callBack.onResponse(response);
                        }
                    });
        }

        /**
         * 异步post
         *
         * @param url      post请求地址
         * @param params   post请求参数
         * @param callBack 回调
         */
        @Override
        public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkHttpUtils.post()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callBack.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            callBack.onResponse(response);
                        }
                    });

        }

        /**
         * 下载
         *
         * @param url      下载地址
         * @param path     文件保存路径
         * @param fileName 文件名称
         * @param callback 回调
         */
        @Override
        public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(path, fileName) {
                        @Override
                        public void inProgress(float progress, long total, int id) {
                            super.inProgress(progress, total, id);
                            callback.onProgress(progress, total);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callback.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            callback.onResponse(response);

                        }

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            callback.onBefore();
                        }
                    });

        }
    }


}
