package com.live.feilong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicevideoplayer.NiceVideoPlayer;
import com.nicevideoplayer.NiceVideoPlayerManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.nicevideoplayer.video.adapter.VideoAdapter;
import com.nicevideoplayer.video.adapter.holder.VideoViewHolder;
import com.nicevideoplayer.video.bean.Video;
import com.nicevideoplayer.video.util.DataUtil;
import com.live.feilong.R;

import java.util.List;


public class ShouYe extends Fragment {
    private RefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_live, container, false);
        refreshLayout= (RefreshLayout) view.findViewById(R.id.refreshLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initListener();
    }

    public void initListener(){
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                init();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore(2000);
                init();
            }
        });
    }
    private void init() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        List<Video> data=null;
        try {
            data = DataUtil.getVideoListData();
        }catch(Exception e){
            e.printStackTrace();
        }
        VideoAdapter adapter = new VideoAdapter(getActivity(),data);
        mRecyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Log.i("livetest",data.get(position).getTitle()+"");
//            }
//        });
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

}
