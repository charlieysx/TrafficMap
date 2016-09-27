package com.elims.trafficmap.fragments.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;

/**
 * Created by smile on 2016/9/25.
 */

public interface IMap {

    /**
     * 设置比例尺位置
     */
    void setScalePosition();

    /**
     * 更新地图状态显示面板
     */
    void updatMyLocation(double lat, double lon, boolean isMove, int dir);

    /**
     * 移动地图，使中心点为指定经纬度
     * @param lat
     * @param lon
     */
    void moveMapState(double lat, double lon);

    /**
     * 是否显示地图模式选择
     */
    void showModeSelect(boolean isShow);

    /**
     * 设置地图模式选择图标
     * @param modeIcon
     */
    void setModeIcon(int modeIcon);

    /**
     *
     * 在地图上添加标注信息
     * @param options
     * @return 返回改标注，便于删除移动等操作
     */
    Marker addMaker(OverlayOptions options);

    /**
     * 在对应的edittext中填入字符串
     * @param etId
     * @param text
     */
    void setText(int etId, String text);

    /**
     * 是否显示查找面板
     * @param isShow
     */
    void showSearch(boolean isShow);

    /**
     * 获取当前地图
     * @return
     */
    BaiduMap getBaiduMap();

    /**
     * 关闭导航
     */
    void showCloseGuide(boolean isClose);

    /**
     * 打开查找面板
     */
    void showOpenNav(boolean isOpen);

    /**
     * 显示对话框
     * @param text 显示的内容
     * @param isClose 是否可以点击关闭
     */
    void showDialog(String text, boolean isClose);

    /**
     * 关闭弹出对话框
     */
    void closeDialog();

}
