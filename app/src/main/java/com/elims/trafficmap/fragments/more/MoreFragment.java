package com.elims.trafficmap.fragments.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragment;

/**
 * 服务区activity
 * Created by smile on 2016/9/24.
 */

public class MoreFragment extends BaseFragment implements IMore, View.OnClickListener {

    private MorePresenter mPresenter;
    private LinearLayout ll_about_team;
    private LinearLayout ll_suggestion;
    private LinearLayout ll_check_update;
    private LinearLayout ll_exit;

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
        ll_about_team = (LinearLayout) mView.findViewById(R.id.ll_about_team);
        ll_suggestion = (LinearLayout) mView.findViewById(R.id.ll_suggestion);
        ll_check_update = (LinearLayout) mView.findViewById(R.id.ll_check_update);
        ll_exit = (LinearLayout) mView.findViewById(R.id.ll_exit);

        ll_about_team.setOnClickListener(this);
        ll_suggestion.setOnClickListener(this);
        ll_check_update.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.click(v);
    }

    @Override
    public void exit() {
        getActivity().finish();
    }
}
