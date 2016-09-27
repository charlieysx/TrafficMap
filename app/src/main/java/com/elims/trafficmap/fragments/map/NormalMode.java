package com.elims.trafficmap.fragments.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.elims.trafficmap.R;
import com.elims.trafficmap.adapter.RouteLineAdapter;
import com.elims.trafficmap.utils.searchroute.SearchRoute;

import java.util.List;

/**
 * 正常模式
 * Created by smile on 2016/9/26.
 */

public class NormalMode extends MapModePresenter {


    // 浏览路线节点相关
    private int nodeIndex = -1; // 节点索引,供浏览节点时使用
    private boolean useDefaultIcon = false;
    private TextView popupText = null; // 泡泡view

    private DrivingRouteResult nowResultd = null;

    public NormalMode(Context context, IMap iMap, SearchRoute searchRoute) {
        super(context, iMap, searchRoute);
    }

    @Override
    public void setLocation(BDLocation location, boolean isGuide) {
        super.setLocation(location, isGuide);
    }

    @Override
    public void setMapClick(LatLng latLng) {
        if(isGuiding){
            return ;
        }
        super.setMapClick(latLng);
        Log.i("nm", "click:" + latLng.latitude + "--" + latLng.longitude);
    }

    @Override
    public void setMapLongClick(LatLng latLng) {
        if(isGuiding){
            return ;
        }
        // 重置浏览节点的路线数据
        if(route != null) {
            route = null;
        }
        iMap.getBaiduMap().clear();
        endLatLng = latLng;
        iMap.showDialog("查找点击的位置信息中...", false);
        searchRoute.getReverseGeoCode(latLng);
        super.setMapLongClick(latLng);
        Log.i("nm", "longclick:" + latLng.latitude + "--" + latLng.longitude);
    }

    @Override
    public void setMapPoiClick(MapPoi mapPoi) {
        if(isGuiding){
            return ;
        }
        // 重置浏览节点的路线数据
        if(route != null) {
            route = null;
        }
        iMap.getBaiduMap().clear();
        endLatLng = mapPoi.getPosition();
        iMap.setText(R.id.et_end_text, mapPoi.getName());
        if (mMarker != null) {
            mMarker.remove();
        }
        if (bitmap == null) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_mark);
        }
        MarkerOptions options = new MarkerOptions().position(endLatLng).icon(bitmap).zIndex(5);
        options.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarker = iMap.addMaker(options);
        iMap.showSearch(true);
        Log.i("nm", "poiclick:" + endLatLng.latitude + "--" + endLatLng.longitude);
    }

    @Override
    public boolean startGuide() {
        if (route == null || route.getAllStep() == null) {
            return false;
        }
        isGuiding = true;
        new Thread(){
            @Override
            public void run() {

                new Thread() {
                    @Override
                    public void run() {
                        while (isGuiding && nodeIndex < route.getAllStep().size()) {
                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    move(true);
                                }
                            });
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }.start();

        return true;
    }

    @Override
    public boolean moveT() {

        if(route != null) {
            iMap.updatMyLocation(startLatLng.latitude, startLatLng.longitude, true, 0);

            return true;
        }

        return false;
    }

    private void move(boolean isMove){
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1) {
            return;
        }
        // 获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        int dir = 0;

        if(nodeIndex < route.getAllStep().size() - 1) {
            DrivingRouteLine.DrivingStep step = (DrivingRouteLine.DrivingStep) route.getAllStep().get(nodeIndex);

            nodeLocation = step.getEntrance().getLocation();
            nodeTitle = step.getEntranceInstructions();
            if(nodeIndex > 0) {
                StringBuilder builder = new StringBuilder(nodeTitle);
                String mi = builder.substring(builder.lastIndexOf("-") + 2, builder.length());
                mi = "行驶" + mi;
                builder.delete(builder.lastIndexOf("-") + 2, builder.length());
                builder.append(mi);
                nodeTitle = builder.toString();
            }
            dir = step.getDirection();
        } else {
            nodeLocation = endLatLng;
            nodeTitle = "到达终点";
            dir = 0;
        }
        Log.i("nm", nodeTitle);

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        startLatLng = new LatLng(nodeLocation.latitude, nodeLocation.longitude);
        // 移动节点至中心
        iMap.updatMyLocation(nodeLocation.latitude, nodeLocation.longitude, isMove, dir);

        nodeIndex++;
        if (nodeIndex == route.getAllStep().size()) {
            startLatLng = new LatLng(endLatLng.latitude, endLatLng.longitude);
            endLatLng = null;
            iMap.setText(R.id.et_end_text, "请在地图上选择终点位置");
            iMap.getBaiduMap().clear();
            isGuiding = false;
            iMap.showCloseGuide(false);
            iMap.showOpenNav(true);
            route = null;
        }

        // show popup
        if(popupText == null) {
            popupText = new TextView(mContext);
            popupText.setBackgroundResource(R.drawable.popup);
            popupText.setTextColor(0xFF000000);
            popupText.setPadding(15, 0, 15, 0);
            popupText.setGravity(Gravity.CENTER);
            popupText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iMap.getBaiduMap().hideInfoWindow();
                }
            });
        }
        popupText.setText(nodeTitle);
        iMap.getBaiduMap().showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }

    @Override
    public boolean startSearch() {

        if (startLatLng == null || endLatLng == null) {
            Toast.makeText(mContext, "请选择终点坐标", Toast.LENGTH_SHORT).show();
            iMap.closeDialog();
            return false;
        }

        searchRoute.searchDrivingRoute(startLatLng, endLatLng);

        return true;
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        iMap.closeDialog();
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(mContext, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Log.i("nm", "查找成功");
            nodeIndex = -1;
            if (drivingRouteResult.getRouteLines().size() > 1) {
                nowResultd = drivingRouteResult;

                // 重置浏览节点的路线数据
                if(route != null) {
                    route = null;
                }
                iMap.getBaiduMap().clear();

                MyTransitDlg myTransitDlg = new MyTransitDlg(mContext, drivingRouteResult.getRouteLines(),
                        RouteLineAdapter.Type.DRIVING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        nodeIndex = 0;
                        //设置路线
                        route = nowResultd.getRouteLines().get(position);

                        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(iMap.getBaiduMap());
                        //为路线中的节点添加点击事件
                        iMap.getBaiduMap().setOnMarkerClickListener(overlay);
                        overlay.setData(nowResultd.getRouteLines().get(position));
                        //将路线绘制到地图中
                        overlay.addToMap();
                        //缩放地图，使路线中的所有节点在屏幕可见范围内
                        overlay.zoomToSpan();

                        //添加一个空节点，代表终点
                        DrivingRouteLine.DrivingStep step = new DrivingRouteLine.DrivingStep();
                        route.getAllStep().add(step);
                    }

                });
                myTransitDlg.show();

            } else if (drivingRouteResult.getRouteLines().size() == 1) {
                nodeIndex = 0;
                //设置路线
                route = drivingRouteResult.getRouteLines().get(0);

                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(iMap.getBaiduMap());
                //为路线中的节点添加点击事件
                iMap.getBaiduMap().setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                //将路线绘制到地图中
                overlay.addToMap();
                //缩放地图，使路线中的所有节点在屏幕可见范围内
                overlay.zoomToSpan();

                //添加一个空节点，代表终点
                DrivingRouteLine.DrivingStep step = new DrivingRouteLine.DrivingStep();
                route.getAllStep().add(step);
            }
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_route_start);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_route_end);
            }
            return null;
        }

        @Override
        public boolean onRouteNodeClick(int i) {
            if (route == null || route.getAllStep() == null) {
                return false;
            }
            if (i == -1) {
                return false;
            }
            // 获取节结果信息
            LatLng nodeLocation = null;
            String nodeTitle = null;

            if (i < route.getAllStep().size()) {
                DrivingRouteLine.DrivingStep step = (DrivingRouteLine.DrivingStep) route.getAllStep().get(i);

                nodeLocation = step.getEntrance().getLocation();
                nodeTitle = step.getEntranceInstructions();

                StringBuilder builder = new StringBuilder(nodeTitle);
                String mi = builder.substring(builder.lastIndexOf("-") + 2, builder.length());
                mi = "行驶" + mi;
                builder.delete(builder.lastIndexOf("-") + 2, builder.length());
                builder.append(mi);
                nodeTitle = builder.toString();
            }

            if (nodeLocation == null || nodeTitle == null) {
                return false;
            }
            // show popup
            if(popupText == null) {
                popupText = new TextView(mContext);
                popupText.setBackgroundResource(R.drawable.popup);
                popupText.setTextColor(0xFF000000);
                popupText.setPadding(15, 0, 15, 0);
                popupText.setGravity(Gravity.CENTER);
            }
            popupText.setText(nodeTitle);
            popupText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iMap.getBaiduMap().hideInfoWindow();
                }
            });
            iMap.getBaiduMap().showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

            return true;
        }
    }
}
