package com.elims.trafficmap.fragments.map;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseFragment;
import com.elims.trafficmap.widgets.DialogLoading;

/**
 * 服务区activity
 * Created by smile on 2016/9/24.
 */

public class MapFragment extends BaseFragment implements IMap {

    private MapPresenter mPresenter;
    /**
     * 地图视图
     */
    private MapView mMapView;
    /**
     * 定位
     */
    private LinearLayout ll_location;
    /**
     * 路段记录
     */
    private LinearLayout ll_route;
    /**
     * 放大地图
     */
    private LinearLayout ll_zoomin;
    /**
     * 缩小地图
     */
    private LinearLayout ll_zoomout;
    /**
     * 切换地图模式
     */
    private LinearLayout ll_switch_mode;
    /**
     * 设置背景颜色
     */
    private RelativeLayout rl_map_bg;
    /**
     * 地图模式选择
     */
    private RelativeLayout rl_mode;
    /**
     * 模式选择显示的动画
     */
    private Animation scale_mode_max;
    /**
     * 模式选择隐藏的动画
     */
    private Animation scale_mode_min;
    /**
     * 查找面板显示的动画
     */
    private Animation scale_search_max;
    /**
     * 查找面板隐藏的动画
     */
    private Animation scale_search_min;
    /**
     * 正常模式(图标)
     */
    private LinearLayout ll_mode_normal;
    /**
     * 服务区模式(图标)
     */
    private LinearLayout ll_mode_service;
    /**
     * 加油站模式(图标)
     */
    private LinearLayout ll_mode_gas;
    /**
     * 正常模式(字)
     */
    private LinearLayout ll_mode_text_normal;
    /**
     * 服务区模式(字)
     */
    private LinearLayout ll_mode_text_service;
    /**
     * 加油站模式(字)
     */
    private LinearLayout ll_mode_text_gas;
    /**
     * 模式选择图标
     */
    private ImageView iv_mode;
    /**
     * 查找面板
     */
    private LinearLayout ll_nav_search;
    /**
     * 起点编辑框
     */
    private EditText et_start_text;
    /**
     * 终点编辑框
     */
    private EditText et_end_text;
    /**
     * 路线查找按钮
     */
    private Button btn_search;
    /**
     * 模拟导航按钮
     */
    private Button btn_nav_simulation;
    /**
     * 关闭面板按钮
     */
    private Button btn_close_nav_search;
    /**
     * 打开查找面板
     */
    private Button btn_open_nav;
    /**
     * 关闭导航
     */
    private Button btn_stop_guide;
    /**
     * 弹出的加载中对话框
     */
    private DialogLoading mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mContext = getActivity();
        mActivity = getActivity();

        initWidget();
        initData();

        return mView;
    }

    /**
     * 对控件初始化
     */
    private void initWidget() {
        mPresenter = new MapPresenter(mContext, this);

        mMapView = (MapView) mView.findViewById(R.id.mv_map);
        ll_location = (LinearLayout) mView.findViewById(R.id.ll_location);
        ll_zoomin = (LinearLayout) mView.findViewById(R.id.ll_zoomin);
        ll_zoomout = (LinearLayout) mView.findViewById(R.id.ll_zoomout);
        ll_switch_mode = (LinearLayout) mView.findViewById(R.id.ll_switch_mode);
        rl_map_bg = (RelativeLayout) mView.findViewById(R.id.rl_map_bg);
        rl_mode = (RelativeLayout) mView.findViewById(R.id.rl_mode);
        ll_mode_normal = (LinearLayout) mView.findViewById(R.id.ll_mode_normal);
        ll_mode_service = (LinearLayout) mView.findViewById(R.id.ll_mode_service);
        ll_mode_gas = (LinearLayout) mView.findViewById(R.id.ll_mode_gas);
        iv_mode = (ImageView) mView.findViewById(R.id.iv_mode);
        ll_mode_text_normal = (LinearLayout) mView.findViewById(R.id.ll_mode_text_normal);
        ll_mode_text_service = (LinearLayout) mView.findViewById(R.id.ll_mode_text_service);
        ll_mode_text_gas = (LinearLayout) mView.findViewById(R.id.ll_mode_text_gas);
        ll_nav_search = (LinearLayout) mView.findViewById(R.id.ll_nav_search);
        et_start_text = (EditText) mView.findViewById(R.id.et_start_text);
        et_end_text = (EditText) mView.findViewById(R.id.et_end_text);
        btn_search = (Button) mView.findViewById(R.id.btn_search);
        btn_nav_simulation = (Button) mView.findViewById(R.id.btn_nav_simulation);
        btn_close_nav_search = (Button) mView.findViewById(R.id.btn_close_nav_search);
        btn_open_nav = (Button) mView.findViewById(R.id.btn_open_nav);
        btn_stop_guide = (Button) mView.findViewById(R.id.btn_stop_guide);
        ll_route = (LinearLayout) mView.findViewById(R.id.ll_route);

        ll_location.setOnClickListener(mPresenter);
        ll_zoomin.setOnClickListener(mPresenter);
        ll_zoomout.setOnClickListener(mPresenter);
        ll_switch_mode.setOnClickListener(mPresenter);
        rl_map_bg.setOnClickListener(mPresenter);
        ll_mode_normal.setOnClickListener(mPresenter);
        ll_mode_service.setOnClickListener(mPresenter);
        ll_mode_gas.setOnClickListener(mPresenter);
        ll_mode_text_normal.setOnClickListener(mPresenter);
        ll_mode_text_service.setOnClickListener(mPresenter);
        ll_mode_text_gas.setOnClickListener(mPresenter);
        btn_search.setOnClickListener(mPresenter);
        btn_nav_simulation.setOnClickListener(mPresenter);
        btn_close_nav_search.setOnClickListener(mPresenter);
        btn_open_nav.setOnClickListener(mPresenter);
        btn_stop_guide.setOnClickListener(mPresenter);
        ll_route.setOnClickListener(mPresenter);

        et_start_text.setEnabled(false);
        et_end_text.setEnabled(false);

        scale_mode_max = AnimationUtils.loadAnimation(mContext, R.anim.scale_mode_max);
        scale_mode_min = AnimationUtils.loadAnimation(mContext, R.anim.scale_mode_min);
        scale_search_max = AnimationUtils.loadAnimation(mContext, R.anim.scale_search_max);
        scale_search_min = AnimationUtils.loadAnimation(mContext, R.anim.scale_search_min);

        mDialog = new DialogLoading(mContext, R.style.LoadingDialog);

        //初次进入界面，设置地图模式选择控件跟查找面板不可见
        showModeSelect(false);
        showSearch(false);
        showCloseGuide(false);
        showOpenNav(true);
    }

    /**
     * 初始化一些数据
     */
    private void initData() {
        mPresenter.setBaiduMapView(mMapView);

        //设置百度地图Logo在左下角
        mMapView.setLogoPosition(LogoPosition.logoPostionleftBottom);
        //不显示比例尺
        mMapView.showScaleControl(false);
        //不显示缩小放大控件
        mMapView.showZoomControls(false);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mPresenter.unregisterBaiduReceiver();
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeydown(int keyCode, KeyEvent event) {

        return mPresenter.onKeydown(keyCode, event);
    }

    @Override
    public void updatMyLocation(double lat, double lon, boolean isMove, int dir) {
        Log.i("mf", "latitude:" + lat + "--longitude:" + lon);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(50)
                .direction(dir)
                .latitude(lat)
                .longitude(lon)
                .build();
        mMapView.getMap().setMyLocationData(locData);
        if (isMove) {
            moveMapState(lat, lon);
        }
    }

    @Override
    public void moveMapState(double lat, double lon) {
        LatLng ll = new LatLng(lat, lon);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll);
        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public void setScalePosition() {

        int x = ll_location.getLeft();
        int y = ll_location.getTop() - ll_location.getHeight();
        //设置比例尺可见及其显示的位置
        mMapView.showScaleControl(true);
        mMapView.setScaleControlPosition(new Point(x, y));

        Log.i("mf", "x:" + x + "\ny:" + y);

    }

    @Override
    public void showModeSelect(boolean isShow) {
        int visible = isShow ? View.VISIBLE : View.INVISIBLE;
        //设置背景是否显示，用于控制图层下面的控件是否可以点击
        rl_map_bg.setVisibility(visible);
        //设置背景是否可以点击
        rl_map_bg.setClickable(isShow);
        //设置背景颜色
        rl_map_bg.setBackgroundColor(isShow ? Color.parseColor("#ddffffff") : Color.TRANSPARENT);
        //设置模式选择控件的显示隐藏效果
        rl_mode.startAnimation(isShow ? scale_mode_max : scale_mode_min);
        //设置模式选择的控件是否可以点击
        ll_mode_normal.setClickable(isShow);
        ll_mode_service.setClickable(isShow);
        ll_mode_gas.setClickable(isShow);
        ll_mode_text_normal.setClickable(isShow);
        ll_mode_text_service.setClickable(isShow);
        ll_mode_text_gas.setClickable(isShow);

    }

    @Override
    public void setModeIcon(int modeIcon) {
        iv_mode.setImageResource(modeIcon);
    }

    @Override
    public Marker addMaker(OverlayOptions options) {
        return (Marker) mMapView.getMap().addOverlay(options);
    }

    @Override
    public void setText(int etId, String text) {
        switch (etId) {
            case R.id.et_start_text:
                et_start_text.setText(text);
                break;
            case R.id.et_end_text:
                et_end_text.setText(text);
                break;
            default:
                break;
        }
    }

    @Override
    public void showSearch(boolean isShow) {
        if(isShow && ll_nav_search.getVisibility() == View.VISIBLE) {
            return ;
        }
        int visible = isShow ? View.VISIBLE : View.INVISIBLE;
        //设置查找面板是否可见
        ll_nav_search.setVisibility(visible);
        //设置查找面板的显示隐藏效果
        ll_nav_search.startAnimation(isShow ? scale_search_max : scale_search_min);
        //设置按钮是否可以点击
        ll_nav_search.setClickable(isShow);
        et_start_text.setVisibility(visible);
        et_end_text.setVisibility(visible);
        btn_search.setClickable(isShow);
        btn_nav_simulation.setClickable(isShow);
        btn_close_nav_search.setClickable(isShow);
        showOpenNav(!isShow);
    }

    @Override
    public void showCloseGuide(boolean isClose) {
        int visible_guide = isClose ? View.VISIBLE : View.INVISIBLE;
        btn_stop_guide.setVisibility(visible_guide);
        btn_stop_guide.setClickable(isClose);
    }

    @Override
    public void showOpenNav(boolean isOpen) {
        int visible_nav = isOpen ? View.VISIBLE : View.INVISIBLE;
        btn_open_nav.setVisibility(visible_nav);
        btn_open_nav.setClickable(isOpen);
    }

    @Override
    public BaiduMap getBaiduMap() {
        if(mMapView != null) {
            return mMapView.getMap();
        }
        return null;
    }

    @Override
    public void showDialog(String text, boolean isClose) {
        if(mDialog != null && !mDialog.isShowing()) {
            mDialog.setMessage(text);
            mDialog.setClose(isClose);
            mDialog.show();
        }
    }

    @Override
    public void closeDialog() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
