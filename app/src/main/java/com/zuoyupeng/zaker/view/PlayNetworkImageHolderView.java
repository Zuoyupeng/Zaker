package com.zuoyupeng.zaker.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.HeaderPicTab;
import com.zuoyupeng.zaker.bean.PlayBean;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class PlayNetworkImageHolderView implements Holder<PlayBean.PromoteItem> {
    private ImageView imageView;
    private TextView tvTitle;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View view = View.inflate(context, R.layout.header_item_play,null);
        tvTitle = (TextView) view.findViewById(R.id.header_item_tv);
        imageView = (ImageView) view.findViewById(R.id.header_item_iv);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, PlayBean.PromoteItem date) {
        imageView.setImageResource(R.drawable.ic_default_adimage0);
        Glide.with(context).load(date.promotion_img).into(imageView);
    }
}
