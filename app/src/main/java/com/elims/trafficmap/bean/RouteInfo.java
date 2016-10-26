package com.elims.trafficmap.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by elims on 16/10/26.
 */

public class RouteInfo {
    LatLng latLng;
    String locationName;
    String info;

    public RouteInfo(LatLng latLng, String locationName, String info) {
        this.latLng = latLng;
        this.locationName = locationName;
        this.info = info;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
