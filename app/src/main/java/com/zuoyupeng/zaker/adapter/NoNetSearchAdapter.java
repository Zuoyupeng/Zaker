package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.dao.SQLIteSearchTable;
import com.zuoyupeng.zaker.dao.SQLIteTable;

import java.util.List;

public class NoNetSearchAdapter extends BaseAdapter{

    List<SQLIteSearchTable> list;
    Context context;

    public NoNetSearchAdapter(List<SQLIteSearchTable> list, Context context) {
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
        view = View.inflate(context, R.layout.channel_item,null);
        TextView tv = (TextView) view.findViewById(R.id.channel_item_tv);
        ImageView iv = (ImageView) view.findViewById(R.id.channel_item_iv);
        tv.setText(list.get(i).getTitleChannel());
        Glide.with(context).load(list.get(i).getList_icon()).into(iv);
        return view;
    }

    public void setNotifyList(List<SQLIteSearchTable> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
