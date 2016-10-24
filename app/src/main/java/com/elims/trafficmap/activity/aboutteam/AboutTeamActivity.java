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
        StringBuffer strAbout = new StringBuffer();
        strAbout.append("地图:\n\n");
        strAbout.append("1.普通模式:\n");
        strAbout.append("点击地图上的标志物或长按地图上任意位置,可设置为目的地,起始地使用者所在地\n");
        strAbout.append("设置目的地后即可查询路线、模拟导航(导航可中断)\n");
        strAbout.append("\n2.服务站模式:\n");
        strAbout.append("自动搜索使用者附近服务区,并显示在地图上,目的地只能是地图上标志的服务区\n");
        strAbout.append("其他功能与普通模式相同\n");
        strAbout.append("\n3.加油站模式:\n");
        strAbout.append("自动搜索使用者附近加油站,并显示在地图上,目的地只能是地图上标志的加油站\n");
        strAbout.append("其他功能与普通模式相同\n\n");
        strAbout.append("\n注:所以路线查询都有提供距离、红路灯数、时长,可供使用者参考\n\n\n");
        strAbout.append("车载视频:\n");
        strAbout.append("点击右上角加号即可添加车载视频\n");
        strAbout.append("添加需要对方同意共享\n");
        strAbout.append("对方同意之后,视频自动添加在列表中,单击可播放或停止\n");
        tv_about_team.setText(strAbout.toString());
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
