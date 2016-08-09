package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.HotBean;
import com.zuoyupeng.zaker.bean.PlayBean;

import java.util.List;

public class PlayAdapter extends BaseAdapter {

    List<PlayBean.Items> list;
    Context context;

    public PlayAdapter(List<PlayBean.Items> list, Context context) {
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
        view = View.inflate(context, R.layout.play_item, null);
        ImageView iv = (ImageView) view.findViewById(R.id.play_iv);
        TextView tvTitle = (TextView) view.findViewById(R.id.play_tv_title);
        TextView tvDes = (TextView) view.findViewById(R.id.play_tv_des);
        tvTitle.setText(list.get(i).title);
        tvDes.setText(list.get(i).content);
        Glide.with(context).load(list.get(i).url).into(iv);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }

    public void setNotifyList(List<PlayBean.Items> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
