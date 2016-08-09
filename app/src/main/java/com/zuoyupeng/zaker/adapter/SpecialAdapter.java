package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.SpActivity;
import com.zuoyupeng.zaker.activity.WebUrlActivity;
import com.zuoyupeng.zaker.bean.Commbean;
import com.zuoyupeng.zaker.bean.PlayBean;
import com.zuoyupeng.zaker.bean.SpecialBean;

import java.util.List;

public class SpecialAdapter extends BaseAdapter {

    List<SpecialBean.TopicItem> list;
    Context context;

    public SpecialAdapter(List<SpecialBean.TopicItem> list, Context context) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.special_item, null);
        ImageView iv = (ImageView) view.findViewById(R.id.special_item_iv);
        TextView tvIv = (TextView) view.findViewById(R.id.special_item_iv_tv);
        TextView tvTitle = (TextView) view.findViewById(R.id.special_item_tv_title);
        TextView tvOne = (TextView) view.findViewById(R.id.special_item_tv_one);
        TextView tvTwo = (TextView) view.findViewById(R.id.special_item_tv_two);
        if(list.get(i).gallery.size() != 0&&list.get(i).gallery != null){
            Glide.with(context).load(list.get(i).gallery.get(0).promotion_img).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            tvIv.setText(list.get(i).gallery.get(0).title);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebUrlActivity.class);
                    intent.putExtra("weburl",list.get(i).gallery.get(0).weburl);
                    context.startActivity(intent);
                }
            });
        }
        tvTitle.setText(list.get(i).entrance.get(0).block_title);
        tvOne.setText(list.get(i).article.get(0).title);
        tvTwo.setText(list.get(i).article.get(1).title);

        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebUrlActivity.class);
                intent.putExtra("weburl",list.get(i).article.get(0).weburl);
                context.startActivity(intent);
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebUrlActivity.class);
                intent.putExtra("weburl",list.get(i).article.get(1).weburl);
                context.startActivity(intent);
            }
        });
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpActivity.class);
                intent.putExtra("api_url",list.get(i).entrance.get(0).api_url);
                intent.putExtra("titleNews",list.get(i).entrance.get(0).block_title);
                intent.putExtra("promotion_img",list.get(i).gallery.get(0).promotion_img);
                intent.putExtra("weburl",list.get(i).gallery.get(0).weburl);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public void setNotifyList(List<SpecialBean.TopicItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
