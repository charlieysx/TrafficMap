package com.elims.trafficmap.fragments.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elims.trafficmap.R;
import com.elims.trafficmap.bean.RouteInfo;

import java.util.List;

/**
 * Created by elims on 16/10/26.
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<RouteInfo> lists;

    public MyAdapter(Context context, List<RouteInfo> lists) {
        mInflater = LayoutInflater.from(context);
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public RouteInfo getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tv_name;
        TextView tv_info;
        convertView = mInflater.inflate(R.layout.item_route_info, null);
        tv_name = (TextView) convertView.findViewById(R.id.tv_location_name);
        tv_info = (TextView) convertView.findViewById(R.id.tv_location_info);
        tv_name.setText(lists.get(position).getLocationName());
        tv_info.setText(lists.get(position).getInfo());

        return convertView;
    }
}
