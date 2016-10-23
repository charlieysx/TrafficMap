package com.elims.trafficmap.fragments.video;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elims.trafficmap.App;
import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragment;

/**
 * 服务区activity
 * Created by smile on 2016/9/24.
 */

public class VideoFragment extends BaseFragment implements IVideo, View.OnClickListener, AdapterView
        .OnItemLongClickListener, AbsListView.OnScrollListener {

    private VideoPresenter mPresenter;
    private ImageView iv_add_video;
    private ListView lv_video;
    private TextView tv_empty_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        initWidget();

        return mView;
    }

    /**
     * 对控件初始化
     */
    private void initWidget() {
        mPresenter = new VideoPresenter(getActivity(), this);

        iv_add_video = (ImageView) mView.findViewById(R.id.iv_add_video);
        lv_video = (ListView) mView.findViewById(R.id.lv_video);
        tv_empty_list = (TextView) mView.findViewById(R.id.tv_empty_list);

        iv_add_video.setOnClickListener(this);
        lv_video.setOnItemLongClickListener(this);
        lv_video.setOnScrollListener(this);
        mPresenter.init(lv_video);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_video:
                Log.i("videoTest", "添加视频");
                mPresenter.addVideo();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i("videoTest", "删除第" + position + "项");
        mPresenter.removeVideo(position);

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (App.sInstance.prePosition < firstVisibleItem || App.sInstance.prePosition >= firstVisibleItem +
                visibleItemCount) {
            if (App.sInstance.preVideoBean != null) {
                App.sInstance.preVideoBean.setPlaying(false);
                App.sInstance.preVideoBean = null;
            }
            if (App.sInstance.preVideoHolder != null) {
                App.sInstance.preVideoHolder.ll_play.setVisibility(View.VISIBLE);
                App.sInstance.preVideoHolder = null;
            }
            if (App.sInstance.preVideoView != null) {
                App.sInstance.preVideoView.stop();
                App.sInstance.preVideoView = null;
            }
            App.sInstance.prePosition = -1;
            Log.i("onScroll", firstVisibleItem + "--" + visibleItemCount + "--" + totalItemCount);
        }
    }

    @Override
    public void setEmptyView(boolean is) {
        if (is) {
            tv_empty_list.setVisibility(View.VISIBLE);
        } else {
            tv_empty_list.setVisibility(View.GONE);
        }
    }
}
