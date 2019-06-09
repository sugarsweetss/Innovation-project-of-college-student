package com.live.feilong.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.live.feilong.activity.MainActivity;
import com.live.feilong.utils.FeilongHelper;
import com.live.feilong.R;
import com.live.feilong.utils.MyActivityList;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends Activity {


    private Button getValidataButton;
    private Handler handler;
    private ImageView backImage;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText passwordAgainEdit;
    private EditText validataEdit;
    private EditText nicknameEdit;
    private Button signInButton;
    private static final int REQUEST_VLIDATE=0;
    private static final int COOL_TIME_OVER=1;
    private static final int VALIDATE_SUCCESS=2;
    private static final int VALIDATE_FAIL=3;
    private static final int REGISTER_SUCCESS=4;
    private static final int REGISTER_FAIL=5;
    private static final int SERVER_FAIL=6;
    private static final int LOCALE_FAIL=7;
    private FeilongHelper feilongHelper;
    private String error=null;
    private HashMap<String,String> regiterInfoMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyActivityList.getInstance().addActivity(this);
        initView();
        initListener();
        initHandler();
    }
    public void initView(){
        feilongHelper =new FeilongHelper();
        getValidataButton= (Button) findViewById(R.id.validata_button);
        backImage= (ImageView) findViewById(R.id.back_image);
        signInButton= (Button) findViewById(R.id.signin_button);
        accountEdit= (EditText) findViewById(R.id.account_edit);
        passwordEdit= (EditText) findViewById(R.id.password_edit);
        passwordAgainEdit= (EditText) findViewById(R.id.password_again_edit);
        validataEdit= (EditText) findViewById(R.id.validata_edit);
        nicknameEdit= (EditText) findViewById(R.id.nickname_edit);
    }
    public void initListener(){
        getValidataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo 向服务器发送请求验证码
                String account=accountEdit.getText().toString();
                if(account==null||account.equals("")){
                    Toast.makeText(Register.this,"请填写邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }
                new requestValidate().execute(account);
                getValidataButton.setClickable(false);
                getValidataButton.setBackground(getResources().getDrawable(R.color.gray));
                new sendTask().execute();

            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                String passwordAgain=passwordAgainEdit.getText().toString();
                String nickName=nicknameEdit.getText().toString();
                String validate=validataEdit.getText().toString();
                if(account==null||account.equals("")){
                    Toast.makeText(Register.this,"账户不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password==null||password.equals("")){
                    Toast.makeText(Register.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordAgain==null||!password.equals(passwordAgain)){
                    Toast.makeText(Register.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nickName==null||nickName.equals("")){
                    Toast.makeText(Register.this,"用户昵称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validate==null||validate.equals("")){
                    Toast.makeText(Register.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                regiterInfoMap.put("account",account);
                regiterInfoMap.put("password",password);
                regiterInfoMap.put("nickname",nickName);
                regiterInfoMap.put("validate",validate);
                try{
                    new registerThread().run();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case REQUEST_VLIDATE:
                        //ToDo 刷新发送验证码时间
                        int i=msg.arg1;
                        getValidataButton.setText(i+"秒之后重发");
                        break;
                    case COOL_TIME_OVER:
                        refreshValidata();
                        break;
                    case VALIDATE_SUCCESS://验证码发送成功
                        Toast.makeText(Register.this,"验证码已发送，请注意查收",Toast.LENGTH_SHORT).show();
                        break;
                    case VALIDATE_FAIL://验证码发送失败
                        Toast.makeText(Register.this,"验证码发送失败",Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_FAIL:
                        Toast.makeText(Register.this,"服务器出错啦",Toast.LENGTH_SHORT).show();
                        break;
                    case REGISTER_FAIL:
                        Toast.makeText(Register.this,error,Toast.LENGTH_SHORT).show();
                        break;
                    case REGISTER_SUCCESS:
                        Toast.makeText(Register.this,"注册成功，已自动登录",Toast.LENGTH_LONG).show();
                        break;
                    case LOCALE_FAIL:
                        Toast.makeText(Register.this,"JSON解析失败(注册成功，但是发生了未知错误)",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }


    public class requestValidate extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... strings) {
            OkHttpClient client=new OkHttpClient();
            String url="http://112.74.182.83/liveshow/mesvalidate";
            RequestBody body=new FormBody.Builder().add("mailbox",strings[0]).build();
            Request request=new Request.Builder().url(url).post(body).build();
            //注意，AsyncTask严格不能在里面执行任何和view主线程有关的工作
            try {
                Response response=client.newCall(request).execute();
                Message message=new Message();
                message.what=VALIDATE_SUCCESS;
                handler.sendMessage(message);
                Log.i("livetest",response.body().string());
            } catch (IOException e) {
                Message message=new Message();
                message.what=VALIDATE_FAIL;
                handler.sendMessage(message);
                e.printStackTrace();
            }
            return null;
        }
    }

    public class sendTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int i=60;
            while(i!=0){
                Message message=new Message();
                message.what=0;
                message.arg1=i;
                handler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);
            return null;
        }
    }

    public void refreshValidata(){
        getValidataButton.setClickable(true);
        getValidataButton.setBackground(getResources().getDrawable(R.drawable.background_login));
        getValidataButton.setText("获取验证码");
    }

    public class registerThread implements Runnable{

        @Override
        public void run() {
            String url="http://112.74.182.83/liveshow/register";
            String result= feilongHelper.postURL(url,regiterInfoMap);
            if(result==null){
                feilongHelper.sendMessage(handler,SERVER_FAIL);
                return;
            }
            String res= feilongHelper.analyseErrorResult(result);
            if(res!=null&&res.equalsIgnoreCase("ok")){
                //todo 处理完成注册后的事
                String loginUrl="http://112.74.182.83/liveshow/login";
                Log.i("livetest","处理之后的事"+loginUrl);
                String jsonResult= feilongHelper.postURL(loginUrl,regiterInfoMap);
                HashMap<String,Object> jmap= JSON.parseObject(jsonResult,new TypeReference<HashMap<String, Object>>(){});
                SharedPreferences sharedPreferences=getSharedPreferences("user-info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token",jmap.get("token").toString());
                editor.putString("nickName",jmap.get("nickName").toString());
                editor.putString("mailbox",jmap.get("mailbox").toString());
                editor.putBoolean("isOnLine",true);
                editor.commit();
                feilongHelper.sendMessage(handler,REGISTER_SUCCESS);
                Intent intent=new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                return;
            }
            if(res!=null){
                error=res;
                feilongHelper.sendMessage(handler,REGISTER_FAIL);
            }
            if(res==null){
                feilongHelper.sendMessage(handler,LOCALE_FAIL);
            }
            return;
        }
    }




}
