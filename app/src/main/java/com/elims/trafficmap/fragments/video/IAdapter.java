package com.elims.trafficmap.fragments.video;

/**
 * Created by elims on 16/10/23.
 */

public interface IAdapter {
    void stop(VideoListAdapter.VideoHolder holder, int position);
    void full(VideoListAdapter.VideoHolder holder, int position);
}
