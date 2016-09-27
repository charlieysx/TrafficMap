package com.elims.trafficmap.fragments.more;

import android.content.Context;

import com.elims.trafficmap.base.BasePresenter;

/**
 * Created by smile on 2016/9/24.
 */

public class MorePresenter extends BasePresenter {

    private IMore serviceArea;

    public MorePresenter(Context context, IMore iMap) {
        super(context);
        this.serviceArea = iMap;
    }

}
