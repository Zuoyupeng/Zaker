package com.zuoyupeng.zaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zuoyupeng.zaker.fragment.BaseFragment;

import java.util.List;

public class ReadPagerFragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> list;

    public ReadPagerFragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setList(List<BaseFragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<BaseFragment> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
