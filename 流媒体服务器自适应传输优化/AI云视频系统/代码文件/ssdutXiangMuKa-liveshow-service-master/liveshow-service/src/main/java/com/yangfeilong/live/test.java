package com.yangfeilong.live;

import okhttp3.*;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavin on 17-9-20.
 */
public class test {
    public static void main(String[] args){
//        Login();
//        Logout();
//        UploadFileTest();
//        DeleteFileTest();
//        MakeFileTest();
//        showDecode();
//        testRandom();
//        liveOn();
//        liveOff();
        uploadTempComment();
//        deleteTempComment();
    }

    public static  void Login(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder().add("account","root5").add("password","201592009").build();
        Request request=new Request.Builder().url("http://localhost:8080/login").post(formBody).build();
//        Request request=new Request.Builder().url("http://112.74.182.83/liveshow/login").post(formBody).build();

        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Logout(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder().add("token","11f6ee0286566").build();
        Request request=new Request.Builder().url("http://localhost:8080/logout").post(formBody).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void UploadFileTest(){
        OkHttpClient client=new OkHttpClient();
        File file1=new File("/home/gavin/db.sql");
        File file2=new File("/home/gavin/db.sql");
        ArrayList<File> list=new ArrayList<>();
        list.add(file1);
        list.add(file2);
        RequestBody body=new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token","3e00e17b39b54e86b886e274ef25ac8f")
                .addFormDataPart("video_name","草拟内")
                .addFormDataPart("description","不好看")
                .addFormDataPart("duration","1000")
                .addFormDataPart("file1","1.sql",RequestBody.create(MediaType.parse("image/png"),new File("/home/gavin/db.sql")))
                .addFormDataPart("fiel2","2.sql",RequestBody.create(MediaType.parse("image/png"),new File("/home/gavin/db.sql")))
                .build();
        Request request=new Request.Builder().url("http://localhost:8080/upload").post(body).build();

        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteFileTest()  {
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("token","72c24df65b054fcfa652336db903ccb8")
                .add("video_id","19")
                .build();
//        Request request=new Request.Builder().url("http://localhost:8080/delete").post(body).build();
        Request request=new Request.Builder().url("http://112.74.182.83/liveshow/delete").post(body).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MakeFileTest(){
        String filePath="/home/gavin/localvideo/root1";
        File file=new File(filePath);
        file.mkdirs();
    }

    public static void showDecode(){
        System.getProperties().list(System.out);
    }

    public static void testRandom(){
        StringBuffer stringBuffer=new StringBuffer("");
        for(int i=0;i<4;++i){
            stringBuffer.append((int)(Math.random()*10));
        }
        System.out.println(stringBuffer.toString());
    }

    public static void liveOn()  {
        String url="http://localhost:8080/live/liveon";
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder().add("token","11f6ee04aecb4be8bf5644c105286566").build();
        Request request=new Request.Builder().post(body).url(url).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void liveOff(){
        String url="http://localhost:8080/live/liveoff";
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder().add("token","11f6ee04aecb4be8bf5644c105286566").build();
        Request request=new Request.Builder().post(body).url(url).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadTempComment(){
        String url="http://localhost:8080/temp_comments/uploadTempComments";
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("nickName","dragon")
                .add("comment","你真帅")
                .add("positive","true")
                .build();
        Request request=new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTempComment(){
        String url="http://localhost:8080/temp_comments/deleteTempComments";
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("nickName","dragon")
                .build();
        Request request=new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}




















