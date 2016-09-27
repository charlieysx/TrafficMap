package com.elims.trafficmap.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by smile on 2016/9/25.
 */

public class BaseFragment extends Fragment {

    protected View mView;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 处理按键点击事件
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeydown(int keyCode, KeyEvent event) {
        return false;
    }

}
