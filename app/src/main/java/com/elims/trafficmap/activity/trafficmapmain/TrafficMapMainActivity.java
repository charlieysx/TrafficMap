package com.elims.trafficmap.activity.trafficmapmain;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragmentActivity;
import com.elims.trafficmap.bean.Tab;
import com.elims.trafficmap.fragments.map.MapFragment;
import com.elims.trafficmap.fragments.more.MoreFragment;
import com.elims.trafficmap.fragments.video.VideoFragment;
import com.elims.trafficmap.widgets.FragmentTabHost;

/**
 * Created by smile on 2016/9/24.
 */

public class TrafficMapMainActivity extends BaseFragmentActivity implements ITrafficMapMain {

    private FragmentTabHost tabHost;
    /**
     * 每个tab对于的fragment
     */
    private static Class<?>[] fragments = {MapFragment.class, VideoFragment.class, MoreFragment.class};
    /**
     * 每个tab对于的标题
     */
    private static int[] tab_titles = {R.string.map, R.string.car_video, R.string.more};
    /**
     * 每个tab对于的图标
     */
    private static int[] tab_icons = {R.drawable.icon_map_selector, R.drawable.icon_video_selector, R.drawable
            .icon_more_selector};

    private TrafficMapMainPresenter trafficMapMainPresenter;

    /**
     * 对控件初始化
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_traffic_map_main);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    }

    @Override
    public void initData() {
        trafficMapMainPresenter = new TrafficMapMainPresenter(this, this);

        initTabs();
    }

    /**
     * 初始化底部导航
     */
    private void initTabs() {

        //调用setup()方法，设置FragmentManager，以及指定用于装载Fragment的布局容器
        tabHost.setup(this, getSupportFragmentManager(), R.id.rl_realtabcontent);

        for (int i = 0;i < fragments.length;++i) {
            Tab tab = new Tab(fragments[i], tab_titles[i], tab_icons[i]);
            //新建5个TabSpec，并且设置好它的Indicator
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(tab.getTitle()));
            View view = View.inflate(this, R.layout.tab_indicator, null);
            TextView tv_tab_txt = (TextView) view.findViewById(R.id.txt_indicator);
            ImageView iv_tab_icon = (ImageView) view.findViewById(R.id.icon_tab);
            tv_tab_txt.setText(tab.getTitle());
            iv_tab_icon.setImageResource(tab.getIcon());
            tabSpec.setIndicator(view);

            //把每个TabSpec添加到FragmentTabHost里面
            tabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        //去掉分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //设置当前默认的分页为第一页
        tabHost.setCurrentTab(0);

    }
}
