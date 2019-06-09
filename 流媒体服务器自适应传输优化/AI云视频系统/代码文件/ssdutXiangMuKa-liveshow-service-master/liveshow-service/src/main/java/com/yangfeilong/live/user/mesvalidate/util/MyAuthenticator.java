package com.yangfeilong.live.user.mesvalidate.util;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by gavin on 17-9-13.
 */
public class MyAuthenticator extends Authenticator {
    String username=null;
    String password=null;
    public MyAuthenticator(){
    }

    public MyAuthenticator(String username,String password){
        this.username=username;
        this.password=password;
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(username,password);
    }

}
