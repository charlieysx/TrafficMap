package com.elims.trafficmap.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.elims.trafficmap.App;
import com.elims.trafficmap.R;
import com.elims.trafficmap.bean.RouteInfo;

/**
 * Created by elims on 16/10/23.
 */

public class AddDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private EditText et_location;
    private EditText et_add_info;
    private TextView tv_add;

    public AddDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_add_route);

        et_location = (EditText) findViewById(R.id.et_location);
        et_add_info = (EditText) findViewById(R.id.et_add_info);
        tv_add = (TextView) findViewById(R.id.tv_add);
        //        et_location.setEnabled(false);


        tv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_add:
                String location = et_location.getText().toString();
                String info = et_add_info.getText().toString();
                if (location.equals("")) {
                    Toast.makeText(activity, "请输入当前位置", Toast.LENGTH_SHORT).show();
                } else if (info.equals("")) {
                    Toast.makeText(activity, "请输入记录", Toast.LENGTH_SHORT).show();
                } else {
                    App.sInstance.routeInfos.add(new RouteInfo(new LatLng(App.sInstance.nowLatLng.latitude, App
                            .sInstance.nowLatLng.longitude), location, info));
                    Toast.makeText(activity, "提交完成", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
        }
    }

}
