package com.elims.trafficmap.fragments.map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.elims.trafficmap.utils.searchroute.SearchRoute;

/**
 * 加油站模式
 * Created by smile on 2016/9/26.
 */

public class GasMode extends ServiceMode {


    public GasMode(Context context, IMap iMap, SearchRoute searchRoute) {
        super(context, iMap, searchRoute);
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {

        iMap.closeDialog();
        Log.i("sm", "结果");
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(mContext, "抱歉，未找到加油站\n请点击定位重新搜索", Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Log.i("sm", "结果已得到" + poiResult.getAllPoi().size() + "个结果");

            iMap.getBaiduMap().clear();

            //创建PoiOverlay
            PoiOverlay overlay = new MyPoiOverlay(iMap.getBaiduMap());
            //设置overlay可以处理标注点击事件
            iMap.getBaiduMap().setOnMarkerClickListener(overlay);
            //设置PoiOverlay数据
            overlay.setData(poiResult);
            //添加PoiOverlay到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
    }

    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);

            iMap.showDialog("查找该加油站详细信息...", false);
            searchRoute.searchDetail(poi.uid);
//            Toast.makeText(mContext, poi.name, Toast.LENGTH_SHORT).show();

            return true;
        }
    }


    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        iMap.closeDialog();
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO
                .NO_ERROR) {
            Toast.makeText(mContext, "未查询到结果，请定位重新查询", Toast.LENGTH_SHORT).show();

            return;
        }
        String district = reverseGeoCodeResult.getAddressDetail().district;
        iMap.showDialog("在" + district + "中搜索加油站...", false);
        searchRoute.searchInCity(district, "加油站");
    }

}
