package com.zuoyupeng.zaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.zuoyupeng.zaker.fragment.BaseFragment;
import java.util.List;

/**
 * Created by zuo on 2016/7/24.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter{

    List<BaseFragment> list;

    public ViewPagerFragmentAdapter(FragmentManager fm,List<BaseFragment> list) {
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

    public void setList(List<BaseFragment> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
