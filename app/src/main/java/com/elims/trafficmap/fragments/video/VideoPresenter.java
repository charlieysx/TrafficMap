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
    private String[] places = {"广州塔", "海珠广场", "北京路", "长隆"};
    private String[] url = {"车1.mp4", "车2.mp4", "车3.mp4", "车4.mp4"};
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
        mAdapter = new VideoListAdapter(mContext, mLists, iVideo);
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
        if (mLists.size() == 0) {
            iVideo.setEmptyView(true);
        }
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

    public void resume() {
        mAdapter.resume();
    }

    private Handler addHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            iVideo.setEmptyView(false);
            mLists.add(new VideoBean(places[position], "https://git.oschina" +
                    ".net/ysx_xx/videoDownloadTest/raw/master/video/" + url[position]));
            mAdapter.notifyDataSetChanged();
        }
    };

}
