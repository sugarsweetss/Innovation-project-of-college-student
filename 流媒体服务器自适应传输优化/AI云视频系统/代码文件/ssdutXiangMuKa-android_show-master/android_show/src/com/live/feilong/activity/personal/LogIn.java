package com.live.feilong.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.live.feilong.activity.MainActivity;
import com.live.feilong.utils.FeilongHelper;
import com.live.feilong.R;
import com.live.feilong.utils.MyActivityList;

import java.util.HashMap;

public class LogIn extends Activity {


    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button signIn;
    private TextView registerLink;
    private FeilongHelper feilongHelper=new FeilongHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        MyActivityList.getInstance().addActivity(this);
        a();
        initView();
        initListener();
    }

    public void initView(){
        usernameEdit= (EditText) findViewById(R.id.username_edit);
        passwordEdit= (EditText) findViewById(R.id.password_edit);
        signIn= (Button) findViewById(R.id.signin_button);
        registerLink= (TextView) findViewById(R.id.register_link);
    }

    public void initListener(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                HashMap<String,String> maps=new HashMap<>();
                maps.put("account",username);
                maps.put("password",password);
                //String url="http://192.168.69.14:8080/login";
                String url="http://112.74.182.83/liveshow/login";
                String result=feilongHelper.postURL(url,maps);
                HashMap<String,String> map=JSON.parseObject(result,new TypeReference<HashMap<String, String>>(){}.getType());
                if(map.get("state").equals("ok")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("user-info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", map.get("token"));
                    editor.putString("nickName",map.get("nickName"));
                    editor.putString("mailbox",map.get("mailbox"));
                    editor.putBoolean("isOnLine", true);
                    editor.commit();
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    setResult(0, intent);
                    finish();
                }else{
                    Toast.makeText(LogIn.this,"账户或密码错误,请重新输入",Toast.LENGTH_LONG).show();
                }

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn.this,Register.class);
                startActivity(intent);
            }
        });
    }

    public void a() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    }

}
