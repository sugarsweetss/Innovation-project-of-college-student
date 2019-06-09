package com.nicevideoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.anbetter.danmuku.DanMuView;
import com.live.feilong.R;
import com.live.feilong.model.commentView.baseModel.CommentModel;
import com.live.feilong.utils.FeilongHelper;
import com.nicevideoplayer.video.DanMuHelper;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaoJianjun on 2017/6/21.
 * 仿腾讯视频热点列表页播放器控制器.
 */
public class TxVideoPlayerController
        extends NiceVideoPlayerController
        implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        ChangeClarityDialog.OnClarityChangedListener{

    private DanMuView positiveDanMuView=null;
    private DanMuView negativeDanMuView=null;
    private LinearLayout danMuView=null;
    private LinearLayout sendCommentView=null;
    private Button chooseCamp=null;
    private EditText writeComment=null;
    private Button sendComment=null;
    private int camp=0;//camp表示当前观众选择的阵营 0:表示尚未选择阵营 1:表示正方 2:中立 3:反方

    private Context mContext;
    private ImageView mImage;
    private ImageView mCenterStart;

    private LinearLayout mTop;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mBatteryTime;
    private ImageView mBattery;
    private TextView mTime;

    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private TextView mClarity;
    private ImageView mFullScreen;

    private TextView mLength;

    private LinearLayout mLoading;
    private TextView mLoadText;

    private LinearLayout mChangePositon;
    private TextView mChangePositionCurrent;
    private ProgressBar mChangePositionProgress;

    private LinearLayout mChangeBrightness;
    private ProgressBar mChangeBrightnessProgress;

    private LinearLayout mChangeVolume;
    private ProgressBar mChangeVolumeProgress;

    private LinearLayout mError;
    private TextView mRetry;

    private LinearLayout mCompleted;
    private TextView mReplay;
    private TextView mShare;

    private boolean topBottomVisible;
    private CountDownTimer mDismissTopBottomCountDownTimer;

    private List<Clarity> clarities;
    private int defaultClarityIndex;

    private ChangeClarityDialog mClarityDialog;

    private boolean hasRegisterBatteryReceiver; // 是否已经注册了电池广播

    private String videoNameWithType="";
    private String videoName="";
    private String nickName="";
    private boolean isLive=false;
    private ArrayList<HashMap<String,Object>> cmlist=new ArrayList<HashMap<String, Object>>();
    private Handler mHandler;
    public static final int COOL_DOWN=4;//发送一次消息后需要等到10秒才能发下一次消息
    public static final int COOL_START=5;//开始冷却
    public static final int COOL_END=6;//结束冷却
    public static final int RELOAD_COV=7;//重新加载评论


    private FeilongHelper feilongHelper=new FeilongHelper();
    public TxVideoPlayerController(Context context) {
        super(context);
        mContext = context;
        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

        initDanmu();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.tx_video_palyer_controller, this, true);

        danMuView= (LinearLayout) findViewById(R.id.DanMuView);
        positiveDanMuView= (DanMuView) findViewById(R.id.positiveDanmu);
        negativeDanMuView= (DanMuView) findViewById(R.id.negativeDanmu);
        chooseCamp= (Button) findViewById(R.id.chooseCamp);
        writeComment= (EditText) findViewById(R.id.writeComment);
        sendComment= (Button) findViewById(R.id.sendComment);

        mCenterStart = (ImageView) findViewById(R.id.center_start);
        mImage = (ImageView) findViewById(R.id.image);

        mTop = (LinearLayout) findViewById(R.id.top);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mBatteryTime = (LinearLayout) findViewById(R.id.battery_time);
        mBattery = (ImageView) findViewById(R.id.battery);
        mTime = (TextView) findViewById(R.id.time);

        mBottom = (LinearLayout) findViewById(R.id.bottom);
        mRestartPause = (ImageView) findViewById(R.id.restart_or_pause);
        mPosition = (TextView) findViewById(R.id.position);
        mDuration = (TextView) findViewById(R.id.duration);
        mSeek = (SeekBar) findViewById(R.id.seek);
        mFullScreen = (ImageView) findViewById(R.id.full_screen);
        mClarity = (TextView) findViewById(R.id.clarity);
        mLength = (TextView) findViewById(R.id.length);

        mLoading = (LinearLayout) findViewById(R.id.loading);
        mLoadText = (TextView) findViewById(R.id.load_text);

        mChangePositon = (LinearLayout) findViewById(R.id.change_position);
        mChangePositionCurrent = (TextView) findViewById(R.id.change_position_current);
        mChangePositionProgress = (ProgressBar) findViewById(R.id.change_position_progress);

        mChangeBrightness = (LinearLayout) findViewById(R.id.change_brightness);
        mChangeBrightnessProgress = (ProgressBar) findViewById(R.id.change_brightness_progress);

        mChangeVolume = (LinearLayout) findViewById(R.id.change_volume);
        mChangeVolumeProgress = (ProgressBar) findViewById(R.id.change_volume_progress);

        mError = (LinearLayout) findViewById(R.id.error);
        mRetry = (TextView) findViewById(R.id.retry);

        mCompleted = (LinearLayout) findViewById(R.id.completed);
        mReplay = (TextView) findViewById(R.id.replay);
        mShare = (TextView) findViewById(R.id.share);

        sendCommentView= (LinearLayout) findViewById(R.id.sendCommentView);

        mCenterStart.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRestartPause.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mClarity.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        danMuView.setOnClickListener(this);
        chooseCamp.setOnClickListener(this);
        sendComment.setOnClickListener(this);

        this.setOnClickListener(this);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case COOL_DOWN:
                        sendComment.setText(msg.arg1+"秒");
                        break;
                    case COOL_START:
                        sendComment.setClickable(false);
                        break;
                    case COOL_END:
                        sendComment.setClickable(true);
                        sendComment.setText("发送");
                        break;
                    case RELOAD_COV:
                        Log.i("livetest","prepare to do cov");
//                        loadDanMuView(cmlist);
                        Log.i("livetest","Done cov");
                        break;

                }

            }
        };
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public ImageView imageView() {
        return mImage;
    }

    @Override
    public void setImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    @Override
    public void setLenght(long length) {
        mLength.setText(NiceUtil.formatTime(length));
    }

    @Override
    public void setNiceVideoPlayer(INiceVideoPlayer niceVideoPlayer) {
        super.setNiceVideoPlayer(niceVideoPlayer);
        // 给播放器配置视频链接地址
        if (clarities != null && clarities.size() > 1) {
            mNiceVideoPlayer.setUp(clarities.get(defaultClarityIndex).videoUrl, null);
        }
    }

    /**
     * 设置清晰度
     *
     * @param clarities 清晰度及链接
     */
    public void setClarity(List<Clarity> clarities, int defaultClarityIndex) {
        if (clarities != null && clarities.size() > 1) {
            this.clarities = clarities;
            this.defaultClarityIndex = defaultClarityIndex;

            List<String> clarityGrades = new ArrayList<>();
            for (Clarity clarity : clarities) {
                clarityGrades.add(clarity.grade + " " + clarity.p);
            }
            mClarity.setText(clarities.get(defaultClarityIndex).grade);
            // 初始化切换清晰度对话框
            mClarityDialog = new ChangeClarityDialog(mContext);
            mClarityDialog.setClarityGrade(clarityGrades, defaultClarityIndex);
            mClarityDialog.setOnClarityCheckedListener(this);
            // 给播放器配置视频链接地址
            if (mNiceVideoPlayer != null) {
                mNiceVideoPlayer.setUp(clarities.get(defaultClarityIndex).videoUrl, null);
            }
        }
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case NiceVideoPlayer.STATE_IDLE:
                break;
            case NiceVideoPlayer.STATE_PREPARING:
                mImage.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mLoadText.setText("正在准备...");
                mError.setVisibility(View.GONE);
                mCompleted.setVisibility(View.GONE);
                mTop.setVisibility(View.GONE);
                mBottom.setVisibility(View.GONE);
                mCenterStart.setVisibility(View.GONE);
                mLength.setVisibility(View.GONE);
                break;
            case NiceVideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                break;
            case NiceVideoPlayer.STATE_PLAYING:  //开始播放
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                startDismissTopBottomTimer();
                Log.i("livetest","beginToStart");
                try{
                    new DanMuTask().execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case NiceVideoPlayer.STATE_PAUSED:
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                cancelDismissTopBottomTimer();
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PLAYING:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                mLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PAUSED:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                mLoadText.setText("正在缓冲...");
                cancelDismissTopBottomTimer();
                break;
            case NiceVideoPlayer.STATE_ERROR:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mTop.setVisibility(View.VISIBLE);
                mError.setVisibility(View.VISIBLE);
                break;
            case NiceVideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mImage.setVisibility(View.VISIBLE);
                mCompleted.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onPlayModeChanged(int playMode) {
        switch (playMode) {
            case NiceVideoPlayer.MODE_NORMAL:
                sendCommentView.setVisibility(GONE);
                mBack.setVisibility(View.GONE);
                mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
                mFullScreen.setVisibility(View.VISIBLE);
                mClarity.setVisibility(View.GONE);
                mBatteryTime.setVisibility(View.GONE);
                if (hasRegisterBatteryReceiver) {
                    mContext.unregisterReceiver(mBatterReceiver);
                    hasRegisterBatteryReceiver = false;
                }
                break;
            case NiceVideoPlayer.MODE_FULL_SCREEN:
                sendCommentView.setVisibility(VISIBLE);
                mBack.setVisibility(View.VISIBLE);
                mFullScreen.setVisibility(View.GONE);
                mFullScreen.setImageResource(R.drawable.ic_player_shrink);
                if (clarities != null && clarities.size() > 1) {
                    mClarity.setVisibility(View.VISIBLE);
                }
                mBatteryTime.setVisibility(View.VISIBLE);
                if (!hasRegisterBatteryReceiver) {
                    mContext.registerReceiver(mBatterReceiver,
                            new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    hasRegisterBatteryReceiver = true;
                }
                break;
            case NiceVideoPlayer.MODE_TINY_WINDOW:
                sendCommentView.setVisibility(GONE);
                mBack.setVisibility(View.VISIBLE);
                mClarity.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 电池状态即电量变化广播接收器
     */
    private BroadcastReceiver mBatterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                // 充电中
                mBattery.setImageResource(R.drawable.battery_charging);
            } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                // 充电完成
                mBattery.setImageResource(R.drawable.battery_full);
            } else {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int percentage = (int) (((float) level / scale) * 100);
                if (percentage <= 10) {
                    mBattery.setImageResource(R.drawable.battery_10);
                } else if (percentage <= 20) {
                    mBattery.setImageResource(R.drawable.battery_20);
                } else if (percentage <= 50) {
                    mBattery.setImageResource(R.drawable.battery_50);
                } else if (percentage <= 80) {
                    mBattery.setImageResource(R.drawable.battery_80);
                } else if (percentage <= 100) {
                    mBattery.setImageResource(R.drawable.battery_100);
                }
            }
        }
    };

    @Override
    protected void reset() {
        topBottomVisible = false;
        cancelUpdateProgressTimer();
        cancelDismissTopBottomTimer();
        mSeek.setProgress(0);
        mSeek.setSecondaryProgress(0);

        mCenterStart.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);

        mBottom.setVisibility(View.GONE);
        mFullScreen.setImageResource(R.drawable.ic_player_enlarge);

        mLength.setVisibility(View.VISIBLE);

        mTop.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.GONE);

        mLoading.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mCompleted.setVisibility(View.GONE);
    }

    /**
     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
     * UI相关的逻辑都尽量到{@link #onPlayStateChanged}和{@link #onPlayModeChanged}中处理.
     */
    @Override
    public void onClick(View v) {
        if (v == mCenterStart) {
            if (mNiceVideoPlayer.isIdle()) {
                String title=mTitle.getText().toString();
                String[] arrs=title.split("\n");
                if (arrs[0].substring(0, 1).equals("播")) {
                    isLive=false;
                    videoNameWithType=arrs[0].substring(3);
                    videoName=videoNameWithType.substring(0,videoNameWithType.lastIndexOf("."));
                    nickName=arrs[2].substring(4);
                }else{
                    nickName=arrs[0].substring(4);
                    isLive=true;
                }
                mNiceVideoPlayer.start();
            }
        } else if (v == mBack) {
            if (mNiceVideoPlayer.isFullScreen()) {
                mNiceVideoPlayer.exitFullScreen();
            } else if (mNiceVideoPlayer.isTinyWindow()) {
                mNiceVideoPlayer.exitTinyWindow();
            }
        } else if (v == mRestartPause) {
            if (mNiceVideoPlayer.isPlaying() || mNiceVideoPlayer.isBufferingPlaying()) {
                mNiceVideoPlayer.pause();
            } else if (mNiceVideoPlayer.isPaused() || mNiceVideoPlayer.isBufferingPaused()) {
                mNiceVideoPlayer.restart();
            }
        } else if (v == mFullScreen) {
            //todo 跳转到可以评论的播放界面
            if (mNiceVideoPlayer.isNormal() || mNiceVideoPlayer.isTinyWindow()) {
                mNiceVideoPlayer.enterFullScreen();
            } else if (mNiceVideoPlayer.isFullScreen()) {
                mNiceVideoPlayer.exitFullScreen();
            }
        } else if (v == mClarity) {
            setTopBottomVisible(false); // 隐藏top、bottom
            mClarityDialog.show();     // 显示清晰度对话框
        } else if (v == mRetry) {
            mNiceVideoPlayer.restart();
        } else if (v == mReplay) {
            mRetry.performClick();
        } else if (v == mShare) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
        } else if (v == danMuView) {
            if (mNiceVideoPlayer.isPlaying()
                    || mNiceVideoPlayer.isPaused()
                    || mNiceVideoPlayer.isBufferingPlaying()
                    || mNiceVideoPlayer.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible);
            }
        } else if (v== chooseCamp){
            switch (camp){
                case 0:
                case 3:
                    camp=1;
                    chooseCamp.setText("正方");
                    chooseCamp.setBackgroundResource(R.color.blue_light);
                    break;
                case 1:
                    camp=2;
                    chooseCamp.setText("中立");
                    chooseCamp.setBackgroundResource(R.color.orange);
                    break;
                case 2:
                    camp=3;
                    chooseCamp.setText("反方");
                    chooseCamp.setBackgroundResource(R.color.red);
                    break;
                default:
                    break;
            }
        } else if (v==sendComment){
            String position= (String) chooseCamp.getText();
            if(position.equals("选择阵营")){
                Toast.makeText(getContext(),"请先选择一个阵营", Toast.LENGTH_SHORT).show();
                return;
            }
            String message=writeComment.getText().toString();
            if(message==null||message.trim().equals("")){
                Toast.makeText(getContext(),"说说你的想法呗", Toast.LENGTH_SHORT).show();
                return;
            }
            if(position.equals("中立")){
                addMiddleDanMu(getContext(),message);
            }
            if((position.equals("正方")||position.equals("反方"))){
                if(!isLive) {
                    String url = "http://112.74.182.83/liveshow/comments/uploadComments";
                    float second = ((float) mNiceVideoPlayer.getCurrentPosition() / 1000) - 1;
                    HashMap<String, String> postmaps = new HashMap<>();
                    postmaps.put("nickName", nickName);
                    postmaps.put("videoName", videoName);
                    postmaps.put("comment", message);
                    postmaps.put("positive", position.equals("正方") ? "true" : "false");
                    postmaps.put("sec", String.valueOf(second));
                    String str=feilongHelper.postURL(url, postmaps);
                    Log.i("feilongtest",str);
                }else{
                    String url="http://112.74.182.83/liveshow/temp_comments/uploadTempComments";
                    HashMap<String,String> postmaps=new HashMap<>();
                    postmaps.put("nickName",nickName);
                    postmaps.put("comment",message);
                    postmaps.put("positive",position.equals("正方")?"true":"false");
                    feilongHelper.postURL(url,postmaps);
                }
            }
            closeKeyboard(v);
            editTextFocus(writeComment);
            writeComment.setText("");
            new TimeCoolTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }
    public void closeKeyboard(View view){
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void editTextFocus(EditText editText){
        editText.setFocusable(false);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.invalidate();
    }

    @Override
    public void onClarityChanged(int clarityIndex) {
        // 根据切换后的清晰度索引值，设置对应的视频链接地址，并从当前播放位置接着播放
        Clarity clarity = clarities.get(clarityIndex);
        mClarity.setText(clarity.grade);
        long currentPosition = mNiceVideoPlayer.getCurrentPosition();
        mNiceVideoPlayer.releasePlayer();
        mNiceVideoPlayer.setUp(clarity.videoUrl, null);
        mNiceVideoPlayer.start(currentPosition);
    }

    @Override
    public void onClarityNotChanged() {
        // 清晰度没有变化，对话框消失后，需要重新显示出top、bottom
        setTopBottomVisible(true);
    }

    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private void setTopBottomVisible(boolean visible) {
        mTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(mNiceVideoPlayer.isFullScreen()){
            sendCommentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        topBottomVisible = visible;
        if (visible) {
            if (!mNiceVideoPlayer.isPaused() && !mNiceVideoPlayer.isBufferingPaused()) {
                startDismissTopBottomTimer();
            }
        } else {
            cancelDismissTopBottomTimer();
        }
    }

    /**
     * 开启top、bottom自动消失的timer
     */
    private void startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer();
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //todo 在这里填写隔一段时间不操作屏幕自动消失top...
//                    setTopBottomVisible(false);
                }
            };
        }
        mDismissTopBottomCountDownTimer.start();
    }

    /**
     * 取消top、bottom自动消失的timer
     */
    private void cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer.cancel();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mNiceVideoPlayer.isBufferingPaused() || mNiceVideoPlayer.isPaused()) {
            mNiceVideoPlayer.restart();
        }
        long position = (long) (mNiceVideoPlayer.getDuration() * seekBar.getProgress() / 100f);
        mNiceVideoPlayer.seekTo(position);
        startDismissTopBottomTimer();
    }

    @Override
    protected void updateProgress() {
        long position = mNiceVideoPlayer.getCurrentPosition();
        long duration = mNiceVideoPlayer.getDuration();
        int bufferPercentage = mNiceVideoPlayer.getBufferPercentage();
        mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        mSeek.setProgress(progress);
        mPosition.setText(NiceUtil.formatTime(position));
        mDuration.setText(NiceUtil.formatTime(duration));
        // 更新时间
        mTime.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date()));
    }

    @Override
    protected void showChangePosition(long duration, int newPositionProgress) {
        mChangePositon.setVisibility(View.VISIBLE);
        long newPosition = (long) (duration * newPositionProgress / 100f);
        mChangePositionCurrent.setText(NiceUtil.formatTime(newPosition));
        mChangePositionProgress.setProgress(newPositionProgress);
        mSeek.setProgress(newPositionProgress);
        mPosition.setText(NiceUtil.formatTime(newPosition));
    }

    @Override
    protected void hideChangePosition() {
        mChangePositon.setVisibility(View.GONE);
    }

    @Override
    protected void showChangeVolume(int newVolumeProgress) {
        mChangeVolume.setVisibility(View.VISIBLE);
        mChangeVolumeProgress.setProgress(newVolumeProgress);
    }

    @Override
    protected void hideChangeVolume() {
        mChangeVolume.setVisibility(View.GONE);
    }

    @Override
    protected void showChangeBrightness(int newBrightnessProgress) {
        mChangeBrightness.setVisibility(View.VISIBLE);
        mChangeBrightnessProgress.setProgress(newBrightnessProgress);
    }

    @Override
    protected void hideChangeBrightness() {
        mChangeBrightness.setVisibility(View.GONE);
    }

    /**
     *
     * @param context:Activity
     * @param positive:是否是正方.
     * @param string:文本内容
     */
    public void addDanMu(Context context,boolean positive,String string){
        if(positive){
            positiveDanMuView.add(DanMuHelper.createDanmuModel(context,positive,string));
        }else {
            negativeDanMuView.add(DanMuHelper.createDanmuModel(context,positive,string));
        }
    }
    public void addMiddleDanMu(Context context,String string){
        double random=Math.random();
        if(random<0.5){
            positiveDanMuView.add(DanMuHelper.createMiddleDanmuModle(context,string));
        }else{
            negativeDanMuView.add(DanMuHelper.createMiddleDanmuModle(context,string));
        }
    }
    public void initDanmu(){
        //启动弹幕
        positiveDanMuView.prepare();
        negativeDanMuView.prepare();
    }




    public class DanMuTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                while (true) {
                    if (!Thread.interrupted()) {
                        Log.i("livetest", "videoName=" + videoName + "&nickName=" + nickName);
                        OkHttpClient client = new OkHttpClient();
                        FormBody body = new FormBody.Builder()
                                .add("videoName", videoName)
                                .add("nickName", nickName)
                                .build();
                        String url = null;
                        Log.i("livetest","param->videoName:"+videoName+";nickName:"+nickName);
                        if (!isLive) {
                            Log.i("livetest","It's not a live");
                            url = "http://112.74.182.83/liveshow/comments/getComments";
                        } else {
                            Log.i("livetest","It's a live");
                            url = "http://112.74.182.83/liveshow/temp_comments/getTempComments";
                        }
                        Request request = new Request.Builder().url(url).post(body).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String str=response.body().string();
                            Log.i("livetest","response is:"+str);
                            CommentModel comments = JSON.parseObject(str, CommentModel.class);
                            Log.i("livetest","the state is : "+comments.getState());
                            ArrayList<HashMap<String, Object>> list = comments.getCommentList();
                            if ((cmlist.isEmpty() || list.size() != cmlist.size())) {
                                cmlist = list;
                                Message message = new Message();
                                message.what = RELOAD_COV;
                                mHandler.sendMessage(message);
                            }
                            if (isLive) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                long currentTime = convertStringToLong(df.format(new Date()));
                                sendLiveDanMu(cmlist, currentTime);
                            } else {
                                float currentTime = mNiceVideoPlayer.getCurrentPosition() / 1000 - 1;
                                sendDanMu(cmlist, currentTime);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (Thread.interrupted()) {
                            break;
                        }
                    }
                }
                return null;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public long convertStringToLong(String time){
            String arr1[]=time.split(" ");
            String days=arr1[0].split("-")[2];
            int day=Integer.parseInt(days);
            String arr[]=arr1[1].split(":");
            int hour= Integer.parseInt(arr[0]);
            int minute=Integer.parseInt(arr[1]);
            int second=Integer.parseInt(arr[2]);
            long finalTime=day*3600*24+hour*2600+minute*60+second;
            return finalTime;
        }

        public void sendLiveDanMu(ArrayList<HashMap<String,Object>> list, long currentTime){
            for (HashMap<String,Object> map : list){
                String time=map.get("comment_time").toString();
                long finalTime=convertStringToLong(time);
                long dis=currentTime-finalTime;
                Log.i("livetest","the dis = "+dis);
                if(dis>=1&&dis<2){
                    addDanMu(getContext(),map.get("mode").toString().equals("0")?false:true,map.get("content").toString());
                }
            }
        }

        public void sendDanMu(ArrayList<HashMap<String,Object>> list,float currentTime){
            for (HashMap<String,Object> map : list){
                float time= Float.parseFloat(map.get("show_sec").toString());
                float dis=currentTime-time;
                if(dis>=0&&dis<1){
                    addDanMu(getContext(),map.get("mode").toString().equals("0")?false:true,map.get("content").toString());
                }
            }
        }
    }

    public class TimeCoolTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            Message start_message=new Message();
            start_message.what=COOL_START;
            mHandler.sendMessage(start_message);
        }

        @Override
        protected String doInBackground(String... strings) {
            int i=10;
            while(i>=0){
                Message message=new Message();
                message.what=COOL_DOWN;
                message.arg1=i;
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Message end_message=new Message();
            end_message.what=COOL_END;
            mHandler.sendMessage(end_message);
        }
    }

}













