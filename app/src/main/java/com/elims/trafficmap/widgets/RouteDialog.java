package com.elims.trafficmap.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.elims.trafficmap.R;
import com.elims.trafficmap.fragments.map.IRouteDialogClick;

/**
 * Created by elims on 16/10/23.
 */

public class RouteDialog extends AlertDialog implements View.OnClickListener {

    private Activity activity;
    private TextView tv_add_route;
    private TextView tv_see_route;
    private TextView tv_close;
    private IRouteDialogClick iRouteDialogClick;

    public RouteDialog(Activity activity, IRouteDialogClick iRouteDialogClick) {
        super(activity);
        this.activity = activity;
        this.iRouteDialogClick = iRouteDialogClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_route_select);

        tv_add_route = (TextView) findViewById(R.id.tv_add_route);
        tv_see_route = (TextView) findViewById(R.id.tv_see_route);
        tv_close = (TextView) findViewById(R.id.tv_close);

        tv_add_route.setOnClickListener(this);
        tv_see_route.setOnClickListener(this);
        tv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_add_route:
                dismiss();
                iRouteDialogClick.dialogItemClick(tv_add_route);
                break;
            case R.id.tv_see_route:
                dismiss();
                iRouteDialogClick.dialogItemClick(tv_see_route);
                break;
            case R.id.tv_close:
                dismiss();
                break;
        }
    }

}
