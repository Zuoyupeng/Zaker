package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.dao.SQLIteTable;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    List<SQLIteTable> list;
    Context context;
    DbManager dbManager;//数据库
    List<SQLIteTable> all;

    public NewsAdapter(List<SQLIteTable> list, Context context, DbManager dbManager) {
        this.list = list;
        this.context = context;
        this.dbManager = dbManager;
        try {
            this.all = dbManager.findAll(SQLIteTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
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
        view = View.inflate(context, R.layout.news_item, null);
        TextView tv = (TextView) view.findViewById(R.id.channel_news_item_tv);
        final ImageView iv = (ImageView) view.findViewById(R.id.channel_news_item_iv);
        final ImageView iv1 = (ImageView) view.findViewById(R.id.channel_news_item_iv1);
        tv.setText(list.get(i).getTitleNews());
        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iv1.setVisibility(View.VISIBLE);//对号显示
                iv.setVisibility(View.GONE);//加号隐藏
                insert(i);//插入数据到主界面
            }
        });
        for (int j = 0; j < all.size(); j++) {
            if (all.get(j).getTitleNews().equals(list.get(i).getTitleNews())) {
                iv1.setVisibility(View.VISIBLE);//对号显示
                iv.setVisibility(View.GONE);//加号隐藏
            }
        }
        return view;
    }

    //插入数据库
    public void insert(int i) {
        try {
            SQLIteTable table = new SQLIteTable();
            table.setTitleCha(list.get(i).getTitleCha());
            table.setTitleNews(list.get(i).getTitleNews());
            table.setPic(list.get(i).getPic());
            table.setApi_url(list.get(i).getApi_url());
            table.setBlock_color(list.get(i).getBlock_color());
            dbManager.save(table);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
