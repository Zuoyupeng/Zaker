package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.BeijingBean;
import com.zuoyupeng.zaker.bean.HotBean;

import java.util.List;

public class LocAdapter extends BaseAdapter{

    List<BeijingBean.Articles> list;
    Context context;

    public LocAdapter(List<BeijingBean.Articles> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(list.get(i).thumbnail_pic != null){
            view = View.inflate(context, R.layout.hot_one_item,null);
            ImageView iv = (ImageView) view.findViewById(R.id.hot_one_item_iv);
            TextView tvTitle = (TextView) view.findViewById(R.id.hot_one_item_tv_title);
            TextView tvFrom = (TextView) view.findViewById(R.id.hot_one_item_tv_from);
            Glide.with(context).load(list.get(i).thumbnail_pic).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            tvTitle.setText(list.get(i).title);
            tvFrom.setText(list.get(i).auther_name);
        }else{
            view = View.inflate(context, R.layout.hot_three_item,null);
            TextView tvTitle = (TextView) view.findViewById(R.id.hot_three_item_tv_title);
            TextView tvFrom = (TextView) view.findViewById(R.id.hot_three_item_tv_from);
            tvTitle.setText(list.get(i).title);
            tvFrom.setText(list.get(i).auther_name);
        }
        return view;
    }

    public void setNotifyList(List<BeijingBean.Articles> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
