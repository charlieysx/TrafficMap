package com.elims.trafficmap.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elims.trafficmap.R;

/**
 * Created by smile on 2016/9/27.
 */

public class DialogLoading extends Dialog {

    private Context mContext;
    /**
     * 是否可以关闭
     */
    private boolean isClose = true;
    /**
     * 对话框显示的文字
     */
    private String message;
    /**
     * 加载项 布局
     */
    private LinearLayout contentView;
    /**
     * 加载项 图标
     */
    private ImageView loadingIcon;
    /**
     * 加载项 提示文字
     */
    private TextView loadingTip;

    /**
     * 默认宽度
     */
    private static int default_width = 140;
    /**
     * 默认高度
     */
    private static int default_height = 140;
    /**
     * 加载项 图标
     */
    private ProgressBar mProgress;


    public DialogLoading(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    /**
     * 使用自定义的主题
     *
     * @param context
     * @param theme
     */
    public DialogLoading(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initView();
    }

    private void initView() {

        contentView = new LinearLayout(mContext);
        contentView.setMinimumHeight(default_width);
        contentView.setMinimumWidth(default_height);
        contentView.setGravity(Gravity.CENTER);
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.loading_bg));

        //设置默认文字
        message = mContext.getResources().getString(R.string.dialog_text);

//        loadingIcon = new ImageView(mContext);
//        loadingIcon.setImageResource(R.drawable.ic_loading);
//        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_repeat);
//        LinearInterpolator lir = new LinearInterpolator();
//        anim.setInterpolator(lir);
//        loadingIcon.setAnimation(anim);

        mProgress = new ProgressBar(mContext);

        loadingTip = new TextView(mContext);
        loadingTip.setGravity(Gravity.CENTER);
        loadingTip.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        loadingTip.setPadding(0, 10, 0, 0);
        loadingTip.setText(message);

        contentView.addView(mProgress);
        contentView.addView(loadingTip);

        setContentView(contentView);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity();
        params.width = (int) (default_width * density);
        params.height = (int) (default_height * density);
        params.gravity = Gravity.CENTER;


        window.setAttributes(params);

    }

    private float getDensity() {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        return dm.density;
    }

    public void setClose(boolean close) {
        isClose = close;
        this.setCanceledOnTouchOutside(close);
    }

    public void setMessage(String mess) {
        this.message = mess;
        loadingTip.setText(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isClose) {
                this.dismiss();
            }
        }
        return true;
    }

}

