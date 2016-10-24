package com.elims.trafficmap.fragments.more;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.elims.trafficmap.R;
import com.elims.trafficmap.activity.aboutteam.AboutTeamActivity;
import com.elims.trafficmap.activity.suggestion.SuggestionActivity;
import com.elims.trafficmap.base.BasePresenter;

import net.steamcrafted.loadtoast.LoadToast;

/**
 * Created by smile on 2016/9/24.
 */

public class MorePresenter extends BasePresenter {

    private IMore iMore;
    private LoadToast lt;

    public MorePresenter(Context context, IMore iMore) {
        super(context);
        this.iMore = iMore;
        lt = new LoadToast(mContext);
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.ll_about_team:
                Intent intent1 = new Intent(iMore.getActivity(), AboutTeamActivity.class);
                iMore.getActivity().startActivity(intent1);
                break;

            case R.id.ll_suggestion:
                Intent intent3 = new Intent(iMore.getActivity(), SuggestionActivity.class);
                iMore.getActivity().startActivity(intent3);
                break;

            case R.id.ll_check_update:
                lt.setText("检查更新...");
                lt.setTranslationY(iMore.getView().getHeight() / 2);
                lt.show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            mHandler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;

            case R.id.ll_exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("是否关闭")
                        .setCancelable(false)
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mHandler.sendEmptyMessage(0);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    iMore.exit();
                    break;
                case 1:
                    lt.success();
                    Toast.makeText(mContext, "当前版本已经是最新版本!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
