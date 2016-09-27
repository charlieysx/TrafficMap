package com.elims.trafficmap.fragments.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragment;

/**
 *
 * 服务区activity
 * Created by smile on 2016/9/24.
 */

public class MoreFragment extends BaseFragment implements IMore {

    private MorePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more, container, false);
        initWidget();

        return mView;
    }

    /**
     * 对控件初始化
     */
    private void initWidget() {
        mPresenter = new MorePresenter(this.getActivity(), this);
    }

}
