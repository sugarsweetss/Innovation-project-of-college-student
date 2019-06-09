package com.live.feilong.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.live.feilong.R;
import com.live.feilong.ui.UIHelper;
import com.live.feilong.ui.viewpagerindicator.CirclePageIndicator;
import com.live.feilong.utils.FeilongHelper;
import com.live.feilong.utils.MyActivityList;


/**
 * Created by tiansj on 15/7/29.
 */
public class SplashActivity extends FragmentActivity {

    private Button btnHome;
    private CirclePageIndicator indicator;
    private ViewPager pager;
    private GalleryPagerAdapter adapter;
    private int[] images = {
            R.drawable.newer01,
            R.drawable.newer02,
            R.drawable.newer03,
            R.drawable.newer04
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MyActivityList.getInstance().addActivity(this);
        SharedPreferences sharedPreferences=getSharedPreferences("user-info",MODE_PRIVATE);
        final boolean firstTimeUse=sharedPreferences.getBoolean("first-time-use",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firstTimeUse) {
                    try{
                        SharedPreferences sharedPreferences=getSharedPreferences("user-info", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("isOnLine",false).commit();
                        Animation fadeOut = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fadeout);
                        fadeOut.setFillAfter(true);
                        findViewById(R.id.guideImage).startAnimation(fadeOut);
                        initGuideGallery();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    UIHelper.showHome(SplashActivity.this);
                }
            }
        }, 1000);
    }



    private void initGuideGallery() {
        final Animation fadeIn= AnimationUtils.loadAnimation(this, R.anim.fadein);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences("user-info",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("first-time-use",false).commit();
                UIHelper.showHome(SplashActivity.this);
            }
        });

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setVisibility(View.VISIBLE);

        adapter = new GalleryPagerAdapter();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    btnHome.setVisibility(View.VISIBLE);
                    btnHome.startAnimation(fadeIn);
                } else {
                    btnHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(SplashActivity.this);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(images[position]);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

}
