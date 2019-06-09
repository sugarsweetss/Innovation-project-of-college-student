package com.live.feilong.utils;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gavin on 17-11-26.
 */

public class MyActivityList extends Application{
    private List<Activity> activityList = null;
    private static MyActivityList instance;

    private MyActivityList(){
        activityList = new LinkedList<Activity>();
    }

    public static MyActivityList getInstance(){
        if(null == instance){
            instance = new MyActivityList();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        if(activityList != null && activityList.size() > 0){
            if(!activityList.contains(activity)){
                activityList.add(activity);
            }
        }else{
            activityList.add(activity);
        }
    }

    public void exit(){
        if(activityList != null && activityList.size()>0){
            for (Activity activity : activityList){
                activity.finish();
            }
        }
        System.exit(0);
    }
}
