package com.nicevideoplayer.video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nicevideoplayer.TxVideoPlayerController;
import com.nicevideoplayer.video.adapter.holder.VideoViewHolder;
import com.nicevideoplayer.video.bean.Video;
import com.live.feilong.R;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Video> mVideoList;
    private OnItemClickListener onItemClickListener=null;

    public VideoAdapter(Context context, List<Video> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        ImageView fullScreen= (ImageView) LayoutInflater.from(mContext).inflate(R.layout.tx_video_palyer_controller,parent,false)
                .findViewById(R.id.full_screen);

        fullScreen.setOnClickListener(this);
        VideoViewHolder holder = new VideoViewHolder(itemView);
//        itemView.findViewById()
        TxVideoPlayerController controller = new TxVideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }


    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.itemView.setTag(position);
        holder.bindData(video);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v, (int) v.getTag());
    }

    public static interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
