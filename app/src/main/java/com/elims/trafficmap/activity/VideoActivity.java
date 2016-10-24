package com.elims.trafficmap.activity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.elims.trafficmap.App;
import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseActivity;
import com.elims.trafficmap.widgets.DialogLoading;
import com.waynell.videolist.widget.TextureVideoView;

/**
 * Created by elims on 16/10/24.
 */

public class VideoActivity extends BaseActivity {

    private TextureVideoView videoplayer;
    private DialogLoading mDialog;

    @Override
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);

        videoplayer = (TextureVideoView) findViewById(R.id.videoplayer);
        mDialog = new DialogLoading(this);
        mDialog.setMessage("连接中...");
    }

    @Override
    protected void initData() {
        if (App.sInstance.videoPath != null) {
            videoplayer.setVideoPath(App.sInstance.videoPath);
            videoplayer.start();
            if (!mDialog.isShowing()) {
                mDialog.show();
                mDialog.setClose(false);
            }
            new Thread() {
                @Override
                public void run() {
                    while (!videoplayer.isPlaying()) {

                    }
                    videoplayer.mute();
                    mDialog.dismiss();
                }
            }.start();
            Log.i("videoactivity", "start--" + App.sInstance.videoPath);
        }
        videoplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        videoplayer.stop();
        super.onDestroy();
    }
}
