package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.CommunityBean;
import com.zuoyupeng.zaker.bean.NewsTab;

import java.util.List;

public class CommunityAdapter extends BaseAdapter {

    List<CommunityBean> list;
    Context context;

    public CommunityAdapter(List<CommunityBean> list, Context context) {
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
        if(view == null){
            view = View.inflate(context, R.layout.community_item, null);
        }
        TextView tvTitle = (TextView) view.findViewById(R.id.community_tv_title);
        TextView tvDes = (TextView) view.findViewById(R.id.community_tv_des);
        ImageView iv = (ImageView) view.findViewById(R.id.community_iv);
        tvTitle.setText(list.get(i).title);
        tvDes.setText(list.get(i).stitle);
        Glide.with(context).load(list.get(i).pic).into(iv);
        return view;
    }

    public void setNotifyList(List<CommunityBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
