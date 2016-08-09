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
import com.zuoyupeng.zaker.bean.Commbean;
import com.zuoyupeng.zaker.bean.HotBean;

import java.util.List;

public class CommAdapter extends BaseAdapter{

    List<Commbean.PostsItem> list;
    Context context;

    public CommAdapter(List<Commbean.PostsItem> list, Context context) {
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
        if(list.get(i).medias != null){
            view = View.inflate(context, R.layout.omm_item_one,null);
            ImageView ivIcon = (ImageView) view.findViewById(R.id.comm_icon_iv);
            ImageView ivImage = (ImageView) view.findViewById(R.id.comm_image_iv);
            TextView tvName = (TextView) view.findViewById(R.id.comm_name_tv);
            TextView tvTitle = (TextView) view.findViewById(R.id.comm_count_tv);
            TextView tvNum = (TextView) view.findViewById(R.id.comm_num_tv);
            if(list.get(i).medias.size() == 0){
                ivImage.setVisibility(View.GONE);
            }else{
                Glide.with(context).load(list.get(i).medias.get(0).url).into(ivImage);
            }
            Glide.with(context).load(list.get(i).icon).into(ivIcon);
            ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
            ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            tvName.setText(list.get(i).name);
            tvTitle.setText(list.get(i).content);
            tvNum.setText(list.get(i).hot_num+"");
        }else{
            view = View.inflate(context, R.layout.omm_item_two,null);
            ImageView ivIcon = (ImageView) view.findViewById(R.id.comm_icon_iv);
            TextView tvName = (TextView) view.findViewById(R.id.comm_name_tv);
            TextView tvTitle = (TextView) view.findViewById(R.id.comm_count_tv);
            TextView tvNum = (TextView) view.findViewById(R.id.comm_num_tv);
            Glide.with(context).load(list.get(i).icon).into(ivIcon);
            tvName.setText(list.get(i).name);
            tvTitle.setText(list.get(i).content);
            tvNum.setText(list.get(i).hot_num+"");
        }
        return view;
    }

    public void setNotifyList(List<Commbean.PostsItem> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
