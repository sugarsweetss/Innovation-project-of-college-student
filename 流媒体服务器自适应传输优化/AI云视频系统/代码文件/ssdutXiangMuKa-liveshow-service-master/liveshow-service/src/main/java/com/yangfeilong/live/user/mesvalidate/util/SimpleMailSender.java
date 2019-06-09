package com.yangfeilong.live.user.mesvalidate.util;


import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by gavin on 17-9-13.
 */
public class SimpleMailSender {
    public boolean sendTextMail(MailSenderInfo mailSenderInfo){
        MyAuthenticator authenticator=null;
        Properties pro=mailSenderInfo.getProperties();
        if(mailSenderInfo.isValidate()){
            authenticator=new MyAuthenticator(mailSenderInfo.getUsername(),mailSenderInfo.getPassword());
        }
        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
        try{
            Message mailMessage=new MimeMessage(sendMailSession);
            Address from=new InternetAddress(mailSenderInfo.getFromAddress());
            Address to=new InternetAddress(mailSenderInfo.getToAddress());
            mailMessage.setFrom(from);
            mailMessage.setRecipient(Message.RecipientType.TO,to);
            mailMessage.setSubject(mailSenderInfo.getSubject());
            mailMessage.setSentDate(new Date());
            mailMessage.setText(mailSenderInfo.getContent());
            Transport.send(mailMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
