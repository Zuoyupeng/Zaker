package com.zuoyupeng.zaker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.WebUrlActivity;
import com.zuoyupeng.zaker.dao.SQLIteReadTable;
import com.zuoyupeng.zaker.dao.XUtilDbRead;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.zuoyupeng.zaker.R.id.read_tv_image_title;

public class ReadFragment extends BaseFragment{

    private String str;
    ImageView ivImage, ivTitle;
    TextView tvOneTitle, tvOneDes, tvTwoTitle, tvTwoDes, tvImageTitle;
    TextView tvThreeTitle, tvThreeDes, tvFourTitle, tvFourDes, tvFiveTitle, tvFiveDes;
    DbManager dbManagerRead;
    List<SQLIteReadTable> list;
    int position = 0;
    LinearLayout llOne,llTwo,llThree,llFour,llFive;
    Intent intent;

    public View initView() {
        View view = View.inflate(getActivity(), R.layout.read_item, null);
        ivImage = (ImageView) view.findViewById(R.id.read_iv_image);
        ivTitle = (ImageView) view.findViewById(R.id.read_iv_title);
        tvOneTitle = (TextView) view.findViewById(R.id.read_tv_one_title);
        tvOneDes = (TextView) view.findViewById(R.id.read_tv_one_des);
        tvTwoTitle = (TextView) view.findViewById(R.id.read_tv_two_title);
        tvTwoDes = (TextView) view.findViewById(R.id.read_tv_two_des);
        tvThreeTitle = (TextView) view.findViewById(R.id.read_tv_three_title);
        tvThreeDes = (TextView) view.findViewById(R.id.read_tv_three_des);
        tvFourTitle = (TextView) view.findViewById(R.id.read_tv_four_title);
        tvFourDes = (TextView) view.findViewById(R.id.read_tv_four_des);
        tvFiveTitle = (TextView) view.findViewById(R.id.read_tv_five_title);
        tvFiveDes = (TextView) view.findViewById(R.id.read_tv_five_des);
        tvImageTitle = (TextView) view.findViewById(read_tv_image_title);
        llOne = (LinearLayout) view.findViewById(R.id.read_lv_one);
        llTwo = (LinearLayout) view.findViewById(R.id.read_lv_two);
        llThree = (LinearLayout) view.findViewById(R.id.read_lv_three);
        llFour = (LinearLayout) view.findViewById(R.id.read_lv_four);
        llFive = (LinearLayout) view.findViewById(R.id.read_lv_five);
        DbManager.DaoConfig daoConfig = XUtilDbRead.getDaoConfig();
        dbManagerRead = x.getDb(daoConfig);
        Bundle bundle = getArguments();
        position = bundle.getInt("pos");
        str = getActivity().getIntent().getStringExtra("TitleNews");
        query();
        return view;
    }

    public void setData() {
        final int p = position * 6;
        if (position >= list.size()/6){
            return;
        }
        Glide.with(this).load(list.get(p).getBgimage_url()).into(ivTitle);
        ivTitle.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this).load(list.get(p).getUrl()).into(ivImage);
        ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
        tvImageTitle.setText(list.get(p).getTitle());
        ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p).getWeburl());
                startActivity(intent);
            }
        });

        tvOneTitle.setText(list.get(1 + p).getTitle());
        tvOneDes.setText(list.get(1 + p).getAuther_name());
        tvTwoTitle.setText(list.get(2 + p).getTitle());
        tvTwoDes.setText(list.get(2 + p).getAuther_name());
        tvThreeTitle.setText(list.get(3 + p).getTitle());
        tvThreeDes.setText(list.get(3 + p).getAuther_name());
        tvFourTitle.setText(list.get(4 + p).getTitle());
        tvFourDes.setText(list.get(4 + p).getAuther_name());
        tvFiveTitle.setText(list.get(5 + p).getTitle());
        tvFiveDes.setText(list.get(5 + p).getAuther_name());
        llOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOneTitle.setTextColor(Color.GRAY);
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p+1).getWeburl());
                startActivity(intent);
            }
        });
        llTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTwoTitle.setTextColor(Color.GRAY);
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p+2).getWeburl());
                startActivity(intent);
            }
        });
        llThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvThreeTitle.setTextColor(Color.GRAY);
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p+3).getWeburl());
                startActivity(intent);
            }
        });
        llFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFourTitle.setTextColor(Color.GRAY);
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p+4).getWeburl());
                startActivity(intent);
            }
        });
        llFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFiveTitle.setTextColor(Color.GRAY);
                intent = new Intent(getActivity(), WebUrlActivity.class);
                intent.putExtra("weburl",list.get(p+5).getWeburl());
                startActivity(intent);
            }
        });
    }

    public void query() {
        try {
            list = dbManagerRead.selector(SQLIteReadTable.class).where(
                    "block_title", "=", str).findAll();
            int count = list.size() % 6;
            for (int i = 0; i < count; i++) {
                list.remove(list.size() - 1);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
