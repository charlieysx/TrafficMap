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

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragment;

/**
 * 服务区activity
 * Created by smile on 2016/9/24.
 */

public class VideoFragment extends BaseFragment implements IVideo, View.OnClickListener, AbsListView
        .OnScrollListener, AdapterView.OnItemLongClickListener {

    private VideoPresenter mPresenter;
    private ImageView iv_add_video;
    private ListView lv_video;

    private int firstVisible = 0;
    private int visibleCount = 0;
    private int totalCount = 0;

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
        mPresenter = new VideoPresenter(this.getActivity(), this);

        iv_add_video = (ImageView) mView.findViewById(R.id.iv_add_video);
        lv_video = (ListView) mView.findViewById(R.id.lv_video);

        iv_add_video.setOnClickListener(this);
        lv_video.setOnScrollListener(this);
        lv_video.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_video:
                Log.i("videoTest", "添加视频");
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                Log.e("videoTest", "SCROLL_STATE_FLING");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                Log.e("videoTest", "SCROLL_STATE_IDLE");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");

                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem   当前第一个可见的item
        // visibleItemCount   当前可见的item个数
        if (firstVisible == firstVisibleItem) {
            return;
        }
        firstVisible = firstVisibleItem;
        visibleCount = visibleItemCount;
        totalCount = totalItemCount;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i("videoTest", "删除第" + position + "项");

        return true;
    }
}
