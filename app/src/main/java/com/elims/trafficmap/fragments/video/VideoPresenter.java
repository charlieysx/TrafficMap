package com.elims.trafficmap.fragments.video;

import android.content.Context;

import com.elims.trafficmap.base.BasePresenter;

/**
 * Created by smile on 2016/9/24.
 */

public class VideoPresenter extends BasePresenter {

    private IVideo iVideo;

    public VideoPresenter(Context context, IVideo iVideo) {
        super(context);
        this.iVideo = iVideo;
    }

}
