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

import java.util.List;

public class HotAdapter extends BaseAdapter{

    private final String HOT_TYPE_3_B = "3_b";
    private final String HOT_TYPE_1 = "1";
    private final String HOT_TYPE_1_B = "1_b";

    List<HotBean> list;
    Context context;

    public HotAdapter(List<HotBean> list, Context context) {
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
        if(HOT_TYPE_1.equals(list.get(i).item_type)){
            view = View.inflate(context, R.layout.hot_one_item,null);
            ImageView iv = (ImageView) view.findViewById(R.id.hot_one_item_iv);
            TextView tvTitle = (TextView) view.findViewById(R.id.hot_one_item_tv_title);
            TextView tvFrom = (TextView) view.findViewById(R.id.hot_one_item_tv_from);
            Glide.with(context).load(list.get(i).thumbnail_medias.get(0).url).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            tvTitle.setText(list.get(i).title);
            tvFrom.setText(list.get(i).auther_name);
        }else if(HOT_TYPE_1_B.equals(list.get(i).item_type)){
            view = View.inflate(context, R.layout.hot_four_item,null);
            ImageView iv = (ImageView) view.findViewById(R.id.hot_four_iv);
            TextView tvTitle = (TextView) view.findViewById(R.id.hot_four_item_tv_title);
            TextView tvFrom = (TextView) view.findViewById(R.id.hot_four_item_tv_from);
            Glide.with(context).load(list.get(i).thumbnail_medias.get(0).url).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            tvTitle.setText(list.get(i).title);
            tvFrom.setText(list.get(i).auther_name);
        }else if(HOT_TYPE_3_B.equals(list.get(i).item_type)){
            view = View.inflate(context, R.layout.hot_two_item,null);
            ImageView ivFirst = (ImageView) view.findViewById(R.id.hot_two_iv_first);
            ImageView ivSecond = (ImageView) view.findViewById(R.id.hot_two_iv_second);
            ImageView ivThree = (ImageView) view.findViewById(R.id.hot_two_iv_three);
            TextView tvTitle = (TextView) view.findViewById(R.id.hot_two_tv_title);
            TextView tvFrom = (TextView) view.findViewById(R.id.hot_two_tv_from);
            Glide.with(context).load(list.get(i).thumbnail_medias.get(0).url).into(ivFirst);
            Glide.with(context).load(list.get(i).thumbnail_medias.get(1).url).into(ivSecond);
            Glide.with(context).load(list.get(i).thumbnail_medias.get(2).url).into(ivThree);
            ivFirst.setScaleType(ImageView.ScaleType.FIT_XY);
            ivSecond.setScaleType(ImageView.ScaleType.FIT_XY);
            ivThree.setScaleType(ImageView.ScaleType.FIT_XY);
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

    public void setNotifyList(List<HotBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
