package com.nicevideoplayer.video;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.anbetter.danmuku.model.DanMuModel;
import com.anbetter.danmuku.model.utils.DimensionUtil;
import com.live.feilong.R;

/**
 * Created by gavin on 17-9-23.
 */

public class DanMuHelper {
    public static DanMuModel createDanmuModel(Context context,boolean positive,String comments){

        //创建弹幕对象
        DanMuModel danMuModel=new DanMuModel();
        if(positive){
            danMuModel.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
            danMuModel.textColor= ContextCompat.getColor(context,R.color.blue_light);
            danMuModel.marginLeft= DimensionUtil.dpToPx(context,30);
        }else{
            danMuModel.setDisplayType(DanMuModel.LEFT_TO_RIGHT);
            danMuModel.textColor=ContextCompat.getColor(context,R.color.red);
            danMuModel.marginRight=DimensionUtil.dpToPx(context,30);
        }

        danMuModel.setPriority(DanMuModel.NORMAL);
        //显示文本内容
        danMuModel.textSize= DimensionUtil.spToPx(context,14);
        danMuModel.textMarginLeft=DimensionUtil.dpToPx(context,5);
        danMuModel.text=comments;

        //弹幕背景
        danMuModel.textBackground=ContextCompat.getDrawable(context,R.drawable.corners_danmu);
        danMuModel.textBackgroundMarginLeft=DimensionUtil.dpToPx(context,15);
        danMuModel.textBackgroundPaddingTop=DimensionUtil.dpToPx(context,3);
        danMuModel.textBackgroundPaddingBottom=DimensionUtil.dpToPx(context,3);
        danMuModel.textBackgroundPaddingRight=DimensionUtil.dpToPx(context,15);
        return danMuModel;
    }

    //创建中立评论的弹幕（随机向右或向左）
    public static DanMuModel createMiddleDanmuModle(Context context,String comments){
        DanMuModel danMuModel=new DanMuModel();
        double random=Math.random();
        if(random<0.5){
            danMuModel.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
            danMuModel.marginLeft= DimensionUtil.dpToPx(context,30);
        }else{
            danMuModel.setDisplayType(DanMuModel.LEFT_TO_RIGHT);
            danMuModel.marginRight=DimensionUtil.dpToPx(context,30);
        }
        danMuModel.textColor=ContextCompat.getColor(context,R.color.orange);
        danMuModel.setPriority(DanMuModel.NORMAL);
        danMuModel.textSize=DimensionUtil.spToPx(context,14);
        danMuModel.textMarginLeft=DimensionUtil.dpToPx(context,5);
        danMuModel.text=comments;

        //弹幕背景
        danMuModel.textBackground=ContextCompat.getDrawable(context,R.drawable.corners_danmu);
        danMuModel.textBackgroundMarginLeft=DimensionUtil.dpToPx(context,15);
        danMuModel.textBackgroundPaddingTop=DimensionUtil.dpToPx(context,3);
        danMuModel.textBackgroundPaddingBottom=DimensionUtil.dpToPx(context,3);
        danMuModel.textBackgroundPaddingRight=DimensionUtil.dpToPx(context,15);
        return danMuModel;

    }

}
