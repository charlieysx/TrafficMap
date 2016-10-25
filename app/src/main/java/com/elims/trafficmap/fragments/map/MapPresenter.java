package com.elims.trafficmap.fragments.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BasePresenter;
import com.elims.trafficmap.utils.searchroute.SearchRoute;
import com.elims.trafficmap.widgets.AddDialog;
import com.elims.trafficmap.widgets.RouteDialog;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by smile on 2016/9/24.
 */

public class MapPresenter extends BasePresenter implements View.OnClickListener, BaiduMap.OnMapClickListener,
        BaiduMap.OnMapLongClickListener {

    /**
     * 地图模式
     */
    enum MapMode {
        /**
         * 普通模式
         */
        MODE_NORMAL,
        /**
         * 服务区模式
         */
        MODE_SERVICE,
        /**
         * 加油站模式
         */
        MODE_GAS
    }

    private IMap iMap;
    private MyBaiduSdkReceiver receiver = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private SearchRoute searchRoute;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    /**
     * 当前地图模式,默认为普通模式
     */
    private MapMode nowMode = MapMode.MODE_NORMAL;
    /**
     * 地图模式选择是否有显示
     */
    private boolean isShowMode = false;
    /**
     * 定位请求间隔
     */
    private int scanSpan = 0;
    /**
     * 正常模式
     */
    private NormalMode normalMode;
    /**
     * 服务区模式
     */
    private ServiceMode serviceMode;
    /**
     * 加油站模式
     */
    private GasMode gasMode;
    /**
     * 当前模式
     */
    private MapModePresenter nowModePresenter;

    public MapPresenter(Context context, IMap iMap) {
        super(context);
        this.iMap = iMap;
        searchRoute = new SearchRoute();
        normalMode = new NormalMode(context, iMap, searchRoute);
        serviceMode = new ServiceMode(context, iMap, searchRoute);
        gasMode = new GasMode(context, iMap, searchRoute);
        nowModePresenter = normalMode;
        searchRoute.setISearch(nowModePresenter);
        registerBaiduReceiver();
    }

    public void setBaiduMapView(MapView mMap) {
        this.mMapView = mMap;

        //获取地图
        mBaiduMap = mMapView.getMap();
        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);
        //地图长按事件处理
        mBaiduMap.setOnMapLongClickListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        // 初始地图现在比例
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18));

        //地图加载完的回调
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //地图加载完成后，设置比例尺的位置
                iMap.setScalePosition();
            }
        });

        initLocation();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(mContext);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        initLocInfo();

        iMap.showDialog("定位中...", false);
        //初次进入该界面，默认定位
        mLocationClient.start();
        Log.i("mp", "开始定位...");
    }

    /**
     * 初始定位信息
     */
    private void initLocInfo() {
        //        if (geo == null) {
        //            geo = BitmapDescriptorFactory.fromResource(R.drawable.ic_follow);
        //        }
        //        MyLocationConfiguration configuration = new MyLocationConfiguration(
        //                MyLocationConfiguration.LocationMode.FOLLOWING, true, geo);
        //        mBaiduMap.setMyLocationConfigeration(configuration);
        LocationClientOption option = new LocationClientOption();
        //设置定位模式为高精度模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //这是发起定位请求的间隔，0表示只定位一次
        option.setScanSpan(scanSpan);
        //设置需要地址信息
        option.setIsNeedAddress(true);
        //设置需要手机机头的方向
        option.setNeedDeviceDirect(true);
        //设置使用gps
        option.setOpenGps(true);
        //设置当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //设置需要位置语义化结果， 可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //设置需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);
    }

    public void registerBaiduReceiver() {
        receiver = new MyBaiduSdkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        mContext.registerReceiver(receiver, filter);
    }

    public void unregisterBaiduReceiver() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                //定位
                if (nowModePresenter.isGuiding()) {
                    Toast.makeText(mContext, "导航中...", Toast.LENGTH_SHORT).show();
                } else if (nowModePresenter.moveT()) {

                } else if (!mLocationClient.isStarted()) {
                    iMap.showDialog("定位中...", false);
                    mLocationClient.start();
                } else {
                    nowModePresenter.moveToLocation();
                }

                break;
            case R.id.ll_switch_mode:
                if (nowModePresenter.isGuiding()) {
                    Toast.makeText(mContext, "导航中...", Toast.LENGTH_SHORT).show();
                    break;
                }
                //切换地图模式
                isShowMode = !isShowMode;
                iMap.showModeSelect(isShowMode);
                break;
            case R.id.ll_zoomin:
                //放大地图
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.ll_zoomout:
                //缩小地图
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.rl_map_bg:
                //取消显示地图模式选择
                isShowMode = !isShowMode;
                iMap.showModeSelect(isShowMode);
                break;
            case R.id.ll_mode_normal:
            case R.id.ll_mode_text_normal:
                //正常模式
                if (nowMode != MapMode.MODE_NORMAL) {
                    nowMode = MapMode.MODE_NORMAL;
                    nowModePresenter.clear();
                    nowModePresenter = normalMode;
                    searchRoute.setISearch(nowModePresenter);
                    mBaiduMap.clear();
                    if (!mLocationClient.isStarted()) {
                        mLocationClient.start();
                    }
                    iMap.setModeIcon(R.drawable.icon_mode_normal_selector);
                }
                isShowMode = !isShowMode;
                iMap.showModeSelect(isShowMode);
                break;
            case R.id.ll_mode_service:
            case R.id.ll_mode_text_service:
                //服务区模式
                if (nowMode != MapMode.MODE_SERVICE) {
                    nowMode = MapMode.MODE_SERVICE;
                    nowModePresenter.clear();
                    nowModePresenter = serviceMode;
                    searchRoute.setISearch(nowModePresenter);
                    mBaiduMap.clear();
                    if (!mLocationClient.isStarted()) {
                        mLocationClient.start();
                    }
                    iMap.setModeIcon(R.drawable.icon_mode_service_selector);
                }
                isShowMode = !isShowMode;
                iMap.showModeSelect(isShowMode);
                break;
            case R.id.ll_mode_gas:
            case R.id.ll_mode_text_gas:
                //加油站模式
                if (nowMode != MapMode.MODE_GAS) {
                    nowMode = MapMode.MODE_GAS;
                    nowModePresenter.clear();
                    nowModePresenter = gasMode;
                    searchRoute.setISearch(nowModePresenter);
                    mBaiduMap.clear();
                    if (!mLocationClient.isStarted()) {
                        mLocationClient.start();
                    }
                    iMap.setModeIcon(R.drawable.icon_mode_gas_selector);
                }
                isShowMode = !isShowMode;
                iMap.showModeSelect(isShowMode);
                break;
            case R.id.btn_search:
                //查找路线
                iMap.showDialog("查找中...", false);
                nowModePresenter.startSearch();

                break;
            case R.id.btn_nav_simulation:
                //模拟导航
                if (nowModePresenter.startGuide()) {
                    iMap.showSearch(false);
                    iMap.showOpenNav(false);
                    iMap.showCloseGuide(true);
                } else {
                    Toast.makeText(mContext, "请先选择路线", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_close_nav_search:
                //关闭查找面板
                iMap.showSearch(false);
                iMap.showOpenNav(true);
                break;
            case R.id.btn_open_nav:
                //打开查找面板
                iMap.showOpenNav(false);
                iMap.showSearch(true);
                break;
            case R.id.btn_stop_guide:
                //关闭导航
                Log.i("mp", "关闭导航");
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("是否关闭导航?")
                        .setCancelable(false)
                        .setPositiveButton("关闭导航", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                nowModePresenter.stopGuide();
                                iMap.showCloseGuide(false);
                                iMap.showSearch(true);
                            }
                        })
                        .setNegativeButton("取消关闭", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.ll_route:
                new RouteDialog((Activity) mContext, new IRouteDialogClick() {
                    @Override
                    public void dialogItemClick(View view) {
                        switch (view.getId()) {
                            case R.id.tv_add_route:
                                Log.i(TAG, "添加路段记录");
                                new AddDialog((Activity) mContext).show();
                                break;
                            case R.id.tv_see_route:
                                Log.i(TAG, "查看路线记录");
                                break;
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (nowModePresenter != null) {
            nowModePresenter.setMapClick(latLng);
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        if (nowModePresenter != null) {
            nowModePresenter.setMapPoiClick(mapPoi);
        }

        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (nowModePresenter != null) {
            if (nowMode == MapMode.MODE_NORMAL) {
                nowModePresenter.setMapLongClick(latLng);
            }
        }
    }

    public boolean onKeydown(int keyCode, KeyEvent keyEvent) {

        Log.i("mp", keyCode + "");

        if (keyCode == KeyEvent.KEYCODE_BACK && isShowMode) {
            isShowMode = !isShowMode;
            iMap.showModeSelect(isShowMode);
            return true;
        }

        return false;
    }

    public void destroy() {
        nowModePresenter.destroy();
        //        if (geo != null) {
        //            geo.recycle();
        //        }
    }

    class MyBaiduSdkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                //网络错误
                toast("无网络");
            } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                //key校验失败
                toast("key校验失败");
            }
        }
    }


    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //定位完成，关闭弹出对话框
            iMap.closeDialog();
            if (location == null || mMapView == null) {
                toast("定位失败,请检测你的网络");
                return;
            }
            switch (location.getLocType()) {
                case 61:
                case 65:
                case 66:
                case 161:

                    if (scanSpan < 1000) {
                        nowModePresenter.setLocation(location, true);
                        //小于1000，说明只定位一次，所以停止定位
                        mLocationClient.stop();
                    } else {
                        //大于1000，在本程序表示导航中
                        Log.i("mp", location.getAddrStr());
                        nowModePresenter.setLocation(location, false);
                    }

                    break;
                default:
                    //定位失败
                    toast("定位失败,请检测你的网络\n错误的返回值:" + location.getLocType());
                    //定位失败，直接停止定位，等待用户继续点击定位
                    mLocationClient.stop();

                    break;
            }

        }
    }

    private void toast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

}
