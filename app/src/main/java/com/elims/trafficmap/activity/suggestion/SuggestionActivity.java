package com.elims.trafficmap.activity.suggestion;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.elims.trafficmap.R;
import com.elims.trafficmap.base.BaseActivity;

import net.steamcrafted.loadtoast.LoadToast;

/**
 * Created by elims on 16/10/23.
 */

public class SuggestionActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_view;
    private ImageView iv_back;
    private EditText et_suggestion;
    private Button btn_submit;
    private LoadToast lt;

    @Override
    public void initView() {
        setContentView(R.layout.activity_suggestion);

        ll_view = (LinearLayout) findViewById(R.id.ll_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_suggestion = (EditText) findViewById(R.id.et_suggestion);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        lt = new LoadToast(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_submit:
                lt.setText("正在提交...");
                lt.setTranslationY(ll_view.getHeight() / 2);
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
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    et_suggestion.setText("");
                    lt.success();
                    Toast.makeText(getApplicationContext(), "感谢您的反馈!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
