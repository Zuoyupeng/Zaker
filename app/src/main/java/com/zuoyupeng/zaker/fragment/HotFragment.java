package com.zuoyupeng.zaker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.WebUrlActivity;
import com.zuoyupeng.zaker.adapter.HotAdapter;
import com.zuoyupeng.zaker.bean.HotBean;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class HotFragment extends BaseFragment {

    ListView lv;
    List<HotBean> list = new ArrayList<>();
    HotAdapter adapter;
    SpringView springView;
    ProgressWheel progressWheel;
    ImageView iv;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressWheel.stopSpinning();
            iv.setVisibility(View.VISIBLE);
            onHotNet();
            return false;
        }
    });

    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_hot, null);
        lv = (ListView) view.findViewById(R.id.fragment_hot_lv);
        springView = (SpringView) view.findViewById(R.id.hot_sv);
        iv = (ImageView) view.findViewById(R.id.hot_iv_error);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        return view;
    }

    @Override
    public void setData() {
        progressWheel.setProgress(0.0f);
        progressWheel.setBarColor(Color.BLUE);
        progressWheel.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float v) {
                if (v == 0) {
                    progressWheel.setProgress(1.0f);
                } else if (v == 1) {
                    progressWheel.setProgress(0.0f);
                }
            }
        });
        onHotNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onHotNet();
            }

            @Override
            public void onLoadmore() {
                onHotNetMore();
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
    }

    public void onHotNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_HOT).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.i("HotFragment", "error");
                handler.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onResponse(String s, int i) {
                list = NetJson.hotJSon(s);
                Log.i("HotFragment", "success");
                Log.i("HotFragment", s);
                Log.i("HotFragment", "list" + list);
                adapter = new HotAdapter(list, getActivity());
                lv.setAdapter(adapter);
                adapter.setNotifyList(list);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                        intent.putExtra("weburl", list.get(i).weburl);
                        startActivity(intent);
                    }
                });
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
                iv.setVisibility(View.GONE);
            }
        });
    }

    public void onHotNetMore() {
        OkHttpUtils.get().url(NetWorkUtils.NET_HOT).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.i("HotFragment", "error");
            }

            @Override
            public void onResponse(String s, int i) {
                List<HotBean> listMore = NetJson.hotJSon(s);
                list.addAll(listMore);
                adapter.setNotifyList(list);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                        intent.putExtra("weburl", list.get(i).weburl);
                        startActivity(intent);
                    }
                });
                springView.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
