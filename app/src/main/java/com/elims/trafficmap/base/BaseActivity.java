package com.elims.trafficmap.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by smile on 2016/9/24.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

    }


    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化一些数据
     */
    protected abstract void initData();
}
