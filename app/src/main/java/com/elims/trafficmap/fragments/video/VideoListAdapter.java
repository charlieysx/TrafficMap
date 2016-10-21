package com.elims.trafficmap.fragments.video;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elims.trafficmap.App;
import com.elims.trafficmap.R;
import com.elims.trafficmap.bean.VideoBean;
import com.elims.trafficmap.widgets.DialogLoading;
import com.waynell.videolist.widget.TextureVideoView;

import java.util.List;

/**
 * Created by elims on 16/10/21.
 */

public class VideoListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<VideoBean> videoBeens;
    private DialogLoading mDialog;

    public VideoListAdapter(Context context, List<VideoBean> videoBeens) {
        this.context = context;
        this.videoBeens = videoBeens;
        mInflater = LayoutInflater.from(context);
        mDialog = new DialogLoading(context);
        mDialog.setMessage("连接中...");
    }

    @Override
    public int getCount() {
        return videoBeens.size();
    }

    @Override
    public VideoBean getItem(int position) {
        return videoBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder && (
                (VideoHolder) convertView.getTag()).position == position) {
            return convertView;
        }

        final VideoHolder viewHolder;
        viewHolder = new VideoHolder();
        convertView = mInflater.inflate(R.layout.item_video, null);
        viewHolder.position = position;
        viewHolder.ll_play = (LinearLayout) convertView.findViewById(R.id.ll_play);
        viewHolder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
        viewHolder.jcVideoPlayer = (TextureVideoView) convertView.findViewById(R.id.videoplayer);
        viewHolder.tv_place.setText(videoBeens.get(position).getPlace());
        viewHolder.jcVideoPlayer.setVideoPath(videoBeens.get(position).getVideoUrl());
        convertView.setTag(viewHolder);
        viewHolder.ll_play.setVisibility(View.VISIBLE);
        viewHolder.jcVideoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoBeens.get(position).isPlaying()) {
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                        mDialog.setClose(false);
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            while (!viewHolder.jcVideoPlayer.isPlaying()) {

                            }
                            mDialog.dismiss();
                        }
                    }.start();

                    if (App.sInstance.preVideoView != null) {
                        App.sInstance.preVideoView.stop();
                    }
                    if (App.sInstance.prePosition != -1) {
                        videoBeens.get(App.sInstance.prePosition).setPlaying(false);
                    }
                    if (App.sInstance.preVideoHolder != null) {
                        App.sInstance.preVideoHolder.ll_play.setVisibility(View.VISIBLE);
                    }
                    if (App.sInstance.preVideoBean != null) {
                        App.sInstance.preVideoBean.setPlaying(false);
                    }
                    videoBeens.get(position).setPlaying(true);

                    App.sInstance.preVideoBean = videoBeens.get(position);
                    App.sInstance.preVideoView = viewHolder.jcVideoPlayer;
                    App.sInstance.prePosition = position;
                    App.sInstance.preVideoHolder = viewHolder;

                    viewHolder.ll_play.setVisibility(View.GONE);
                    viewHolder.jcVideoPlayer.start();
                    Log.i("click", "start");
                } else {
                    App.sInstance.preVideoView = viewHolder.jcVideoPlayer;
                    App.sInstance.preVideoBean.setPlaying(false);
                    App.sInstance.preVideoBean = null;
                    App.sInstance.prePosition = -1;
                    App.sInstance.preVideoHolder = null;
                    viewHolder.ll_play.setVisibility(View.VISIBLE);
                    videoBeens.get(position).setPlaying(false);
                    viewHolder.jcVideoPlayer.stop();
                    Log.i("click", "stop");
                }
            }
        });

        return convertView;
    }

    public class VideoHolder {
        int position;
        LinearLayout ll_play;
        TextView tv_place;
        TextureVideoView jcVideoPlayer;
    }
}
