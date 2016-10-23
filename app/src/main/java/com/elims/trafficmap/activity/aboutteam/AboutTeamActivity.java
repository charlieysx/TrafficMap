package com.elims.trafficmap.activity.aboutteam;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseActivity;

/**
 * Created by elims on 16/10/23.
 */

public class AboutTeamActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_about_team;

    @Override
    public void initView() {
        setContentView(R.layout.activity_about_team);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_about_team = (TextView) findViewById(R.id.tv_about_team);

        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}
