package com.elims.trafficmap.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.InfoWindow;
import com.elims.trafficmap.App;
import com.elims.trafficmap.R;
import com.elims.trafficmap.bean.RouteInfo;
import com.elims.trafficmap.fragments.map.MyAdapter;

/**
 * Created by elims on 16/10/23.
 */

public class SeeDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TextView tv_close;
    private ListView listView;

    public SeeDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_see_route);

        listView = (ListView) findViewById(R.id.lv_route_info);
        tv_close = (TextView) findViewById(R.id.tv_close);

        listView.setAdapter(new MyAdapter(activity, App.sInstance.routeInfos));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteInfo routeInfo = App.sInstance.routeInfos.get(position);
                App.sInstance.iMap.moveMapState(routeInfo.getLatLng().latitude, routeInfo.getLatLng().longitude);
                TextView popupText;
                popupText = new TextView(activity);
                popupText.setBackgroundResource(R.drawable.popup);
                popupText.setTextColor(0xFF000000);
                popupText.setPadding(15, 0, 15, 0);
                popupText.setGravity(Gravity.CENTER);
                popupText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App.sInstance.iMap.getBaiduMap().hideInfoWindow();
                    }
                });
                popupText.setText(routeInfo.getInfo());
                App.sInstance.iMap.getBaiduMap().showInfoWindow(new InfoWindow(popupText, routeInfo.getLatLng(), 0));
                dismiss();
            }
        });

        tv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
        }
    }

}
