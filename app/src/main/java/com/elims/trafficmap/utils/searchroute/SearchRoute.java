package com.elims.trafficmap.utils.searchroute;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * 查找相关
 * Created by smile on 2016/9/26.
 */

public class SearchRoute implements OnGetRoutePlanResultListener, OnGetGeoCoderResultListener,
        OnGetPoiSearchResultListener {
    private ISearchRoute iSearchRoute;
    /**
     * 路线搜索
     */
    private RoutePlanSearch mRoutePlanSearch;
    /**
     * 地理编码搜索
     */
    private GeoCoder mGeoCoder;
    /**
     * Poi搜索
     */
    private PoiSearch mPoiSearch;

    public SearchRoute() {
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mGeoCoder = GeoCoder.newInstance();
        mPoiSearch = PoiSearch.newInstance();

        initListener();
    }

    private void initListener() {
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    public void setISearch(ISearchRoute iSearch) {
        this.iSearchRoute = iSearch;
    }

    /**
     * 查询驾车路线
     * @param start
     * @param end
     */
    public void searchDrivingRoute(LatLng start, LatLng end) {
        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
//        Log.i("sr", "开始查找路线:\n" + "start:x:" + start.latitude + "--y:" + start.longitude + "\nend:x:" + end.latitude +
//                "--y:" + end.longitude);
        mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    /**
     * 查找经纬度所在地地名
     * @param point
     */
    public void getReverseGeoCode(LatLng point) {
//        Log.i("sr", "开始查找坐标");
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
    }

    /**
     * 查找周边
     * @param keyWord 关键字
     * @param location 所在经纬度
     * @param radius 查找范围，单位m
     * @param sortType 结果排序(可选，默认是传null)
     *                 comprehensive :综合排序
     *                 distance_from_near_to_far :按距离排序
     */
    public void searchNear(String keyWord, LatLng location, int radius, PoiSortType sortType){

        PoiNearbySearchOption option = new PoiNearbySearchOption()
                .keyword(keyWord)
                .location(location)
                .radius(radius);
        if(sortType != null) {
            option.sortType(sortType);
        }
        Log.i("sr", "开始搜索");
        mPoiSearch.searchNearby(option);
    }

    /**
     * 城市内查找poi
     * @param city
     * @param keyWord
     */
    public void searchInCity(String city, String keyWord) {
        PoiCitySearchOption option = new PoiCitySearchOption()
                .city(city)
                .keyword(keyWord)
                .pageCapacity(10);
        Log.i("sr", "开始搜索");
        mPoiSearch.searchInCity(option);

    }

    public void searchDetail(String poiUid) {
        PoiDetailSearchOption option = new PoiDetailSearchOption()
                .poiUid(poiUid);

        mPoiSearch.searchPoiDetail(option);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (iSearchRoute != null) {
            iSearchRoute.onGetDrivingRouteResult(drivingRouteResult);
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (iSearchRoute != null) {
            iSearchRoute.onGetReverseGeoCodeResult(reverseGeoCodeResult);
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        Log.i("sr", "onGetPoiResult");
        if (iSearchRoute != null) {
            iSearchRoute.onGetPoiResult(poiResult);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Log.i("sr", "onGetPoiDetailResult");
        if (iSearchRoute != null) {
            iSearchRoute.onGetPoiDetailResult(poiDetailResult);
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
