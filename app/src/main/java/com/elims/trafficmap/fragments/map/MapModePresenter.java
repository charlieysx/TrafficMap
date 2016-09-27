package com.elims.trafficmap.fragments.map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BasePresenter;
import com.elims.trafficmap.utils.searchroute.ISearchRoute;
import com.elims.trafficmap.utils.searchroute.SearchRoute;

/**
 * Created by smile on 2016/9/26.
 */

public abstract class MapModePresenter extends BasePresenter implements ISearchRoute {

    protected IMap iMap;
    /**
     * 定位信息
     */
    protected BDLocation location;
    /**
     * 起始经纬度(定位的经纬度)
     */
    protected LatLng startLatLng;
    /**
     * 终点经纬度
     */
    protected LatLng endLatLng;
    /**
     * 地图上的标注
     */
    protected Marker mMarker;
    /**
     * 标注图标
     */
    protected BitmapDescriptor bitmap;
    /**
     * 查找
     */
    protected SearchRoute searchRoute;
    /**
     * 是否导航中
     */
    protected boolean isGuiding = false;
    /**
     * 导航路线
     */
    protected RouteLine route = null;

    public MapModePresenter(Context context, IMap iMap, SearchRoute searchRoute) {
        super(context);
        this.iMap = iMap;
        this.searchRoute = searchRoute;
    }

    /**
     * 设置定位信息
     *
     * @param location
     */
    public void setLocation(BDLocation location, boolean isGuide) {
        this.location = location;
        this.startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //定位成功,更新地图状态显示面板
        iMap.updatMyLocation(location.getLatitude(), location.getLongitude(), isGuide, (int) location
                .getDirection());
    }

    /**
     * 设置地图点击位置
     *
     * @param latLng
     */
    public void setMapClick(LatLng latLng) {

    }

    /**
     * 设置地图长按位置
     *
     * @param latLng
     */
    public void setMapLongClick(LatLng latLng) {
        endLatLng = latLng;
        if (mMarker != null) {
            mMarker.remove();
        }
        if (bitmap == null) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_mark);
        }
        MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).zIndex(5);
        options.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarker = iMap.addMaker(options);
        iMap.showSearch(true);
    }

    /**
     * 切换了模式，清除原来的路线终点等信息
     */
    public void clear() {
        if(route != null) {
            route = null;
        }
        if(endLatLng != null) {
            endLatLng = null;
        }
    }

    public boolean isGuiding() {
        return isGuiding;
    }

    /**
     * 设置地图覆盖物点击位置
     *
     * @param mapPoi
     */
    public void setMapPoiClick(MapPoi mapPoi) {
        endLatLng = mapPoi.getPosition();
        iMap.moveMapState(endLatLng.latitude, endLatLng.longitude);
    }

    /**
     * 把我的位置移到地图中心
     */
    public void moveToLocation() {
        if (startLatLng != null) {
            //定位成功,更新地图状态显示面板
            iMap.updatMyLocation(startLatLng.latitude, startLatLng.longitude, true, (int) location
                    .getDirection());
        }
    }

    /**
     * 如果在模拟导航暂停是定位，则定位的位置为导航暂停的位置
     * @return
     */
    public boolean moveT() {
        return false;
    }

    /**
     * 开始模拟导航
     */
    public boolean startGuide() {
        return false;
    }

    /**
     * 开始查找
     *
     * @return
     */
    public boolean startSearch() {

        return false;
    }

    public void destroy() {
        /**
         * 回收bitmap资源
         */
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        String address;

        iMap.closeDialog();
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO
                .NO_ERROR) {
            Log.i("mmp", "坐标未查找到结果");
            address = "地图上标记的地方";
            iMap.setText(R.id.et_end_text, address);

            return ;
        }
        Log.i("mmp", "坐标查找到结果:" + reverseGeoCodeResult.getAddress());
        address = reverseGeoCodeResult.getAddress();
        if (address.equals("")) {
            address = "地图上标记的地方";
        }
        iMap.setText(R.id.et_end_text, address);
    }

    public void stopGuide() {
        isGuiding = false;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }
}
