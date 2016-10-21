package com.elims.trafficmap.fragments.video;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;

import com.elims.trafficmap.base.BasePresenter;
import com.elims.trafficmap.bean.VideoBean;
import com.elims.trafficmap.widgets.DialogLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smile on 2016/9/24.
 */

public class VideoPresenter extends BasePresenter {

    private IVideo iVideo;
    private ListView videoListView;
    private List<VideoBean> mLists;
    private VideoListAdapter mAdapter;
    private String[] places = {"广州塔", "海珠广场", "北京路", "长隆", "步行街"};
    private String[] url = {"单行的轨道", "泡沫", "睡公主", "我的秘密(钢琴版)", "新的心跳"};
    private int position = -1;
    private DialogLoading mDialog;

    public VideoPresenter(Context context, IVideo iVideo) {
        super(context);
        this.iVideo = iVideo;
        mDialog = new DialogLoading(mContext);
        mDialog.setMessage("等待对方同意...");
        mDialog.setClose(false);
    }

    public void init(ListView lv_video) {
        videoListView = lv_video;
        initData();
        mAdapter = new VideoListAdapter(mContext, mLists);
        videoListView.setAdapter(mAdapter);
    }

    private void initData() {
        mLists = new ArrayList<>();
    }

    public void addVideo() {
        position++;
        if (position < url.length) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("是否添加'" + places[position] + "'处的视频(xx人提供)?")
                    .setCancelable(false)
                    .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            add();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            position--;
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        Log.i("video", "add");
    }

    public void removeVideo(int position) {
        mLists.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private void add() {
        mDialog.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler addHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mLists.add(new VideoBean(places[position], "https://git.oschina" +
                    ".net/ysx_xx/videoDownloadTest/raw/master/video/" + url[position] + "" +
                    ".mkv"));
            mAdapter.notifyDataSetChanged();
        }
    };

}
