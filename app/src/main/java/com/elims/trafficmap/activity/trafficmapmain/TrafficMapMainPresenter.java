package com.elims.trafficmap.activity.trafficmapmain;

import android.content.Context;

import com.elims.trafficmap.base.BasePresenter;

/**
 * Created by smile on 2016/9/25.
 */

public class TrafficMapMainPresenter extends BasePresenter {

    private ITrafficMapMain trafficMapMain;

    public TrafficMapMainPresenter(Context context, ITrafficMapMain trafficMapMain) {
        super(context);
        this.trafficMapMain = trafficMapMain;
    }
}
