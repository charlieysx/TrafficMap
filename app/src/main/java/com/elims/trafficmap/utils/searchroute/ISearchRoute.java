package com.elims.trafficmap.utils.searchroute;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;

/**
 * Created by smile on 2016/9/26.
 */

public interface ISearchRoute {

    /**
     * 查询驾车路线结果
     * @param drivingRouteResult
     */
    void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult);

    /**
     * 根据经纬度查询位置信息
     * @param reverseGeoCodeResult
     */
    void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult);

    /**
     * Poi查询结果
     * @param poiResult
     */
    void onGetPoiResult(PoiResult poiResult);

    /**
     * poi详情查找结果
     * @param poiDetailResult
     */
    void onGetPoiDetailResult(PoiDetailResult poiDetailResult);
}
