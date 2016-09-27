package com.elims.trafficmap.bean;


/**
 * Created by smile on 2016/9/25.
 */

public class Tab {
    /**
     * tab对于的fragment
     */
    private Class<?> fragment;
    /**
     * tab的标题
     */
    private int title;
    /**
     * tab的图标
     */
    private int icon;

    public Tab(Class<?> fragment, int title, int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }

    public Class<?> getFragment() {
        return fragment;
    }

    public void setFragment(Class<?> fragment) {
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
