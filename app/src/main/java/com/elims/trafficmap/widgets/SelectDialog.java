package com.elims.trafficmap.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.elims.trafficmap.R;
import com.elims.trafficmap.fragments.video.IAdapter;
import com.elims.trafficmap.fragments.video.VideoListAdapter;

/**
 * Created by elims on 16/10/23.
 */

public class SelectDialog extends AlertDialog implements android.view.View.OnClickListener {

    private Activity activity;
    private TextView tv_play;
    private TextView tv_full;
    private TextView tv_close;
    private IAdapter iAdapter;
    private int position;
    private VideoListAdapter.VideoHolder holder;

    public SelectDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setData(IAdapter iAdapter, int position, VideoListAdapter.VideoHolder holder) {
        this.iAdapter = iAdapter;
        this.position = position;
        this.holder = holder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select);

        tv_play = (TextView) findViewById(R.id.tv_play);
        tv_full = (TextView) findViewById(R.id.tv_full);
        tv_close = (TextView) findViewById(R.id.tv_close);

        tv_play.setOnClickListener(this);
        tv_full.setOnClickListener(this);
        tv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_play:
                dismiss();
                iAdapter.stop(holder, position);
                break;
            case R.id.tv_full:
                dismiss();
                iAdapter.full(holder, position);
                break;
            case R.id.tv_close:
                dismiss();
                break;
        }
    }

}
