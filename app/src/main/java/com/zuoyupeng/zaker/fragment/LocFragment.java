package com.zuoyupeng.zaker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.WebUrlActivity;
import com.zuoyupeng.zaker.adapter.LocAdapter;
import com.zuoyupeng.zaker.bean.BeijingBean;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.NetWorkUtils;
import com.zuoyupeng.zaker.view.LocNetworkImageHolderView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class LocFragment extends BaseFragment {

    ConvenientBanner convenientBanner;//轮播图
    ListView lv;
    List<BeijingBean> list = new ArrayList<>();
    List<BeijingBean.Gallery> listG;
    List<BeijingBean.Articles> listA = new ArrayList<>();
    LocAdapter adapter;
    Intent intent;
    SpringView springView;
    ProgressWheel progressWheel;
    ImageView iv;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressWheel.stopSpinning();
            iv.setVisibility(View.VISIBLE);
            onLocNet();
            return false;
        }
    });

    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_loc, null);
        lv = (ListView) view.findViewById(R.id.fragment_loc_lv);
        springView = (SpringView) view.findViewById(R.id.loc_sv);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        iv = (ImageView) view.findViewById(R.id.loc_iv_error);
        return view;
    }

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
        onLocNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            public void onRefresh() {
                onLocNetMore();
            }

            public void onLoadmore() {
                onLocNetMore();
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
    }

    public void onLocNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_BEIJING).build().execute(new StringCallback() {
            public void onError(Call call, Exception e, int i) {
                handler.sendEmptyMessageDelayed(0, 3000);
            }

            public void onResponse(String s, int i) {
                list = NetJson.beijingJson(s);
                listA = list.get(0).articles;
                if (list.get(0).gallery != null) {
                    listG = list.get(0).gallery;
                } else {
                    listG = null;
                }
                if (listG != null&&listG.size() !=0) {
                    //引入轮播图布局
                    convenientBanner = (ConvenientBanner) LayoutInflater.from(getActivity())
                            .inflate(R.layout.adapter_header_loc, null);
                    convenientBanner.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 600));
                    showHeaderLayout();
                    adapter = new LocAdapter(listA, getActivity());
                    lv.setAdapter(adapter);
                    lv.addHeaderView(convenientBanner);
                    adapter.setNotifyList(listA);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listA.get(i - 1).weburl);
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter = new LocAdapter(listA, getActivity());
                    lv.setAdapter(adapter);
                    adapter.setNotifyList(listA);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listA.get(i).weburl);
                            startActivity(intent);
                        }
                    });
                }
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
                iv.setVisibility(View.GONE);
            }
        });
    }

    public void onLocNetMore() {
        OkHttpUtils.get().url(NetWorkUtils.NET_BEIJING).build().execute(new StringCallback() {
            public void onError(Call call, Exception e, int i) {

            }

            public void onResponse(String s, int i) {
                list = NetJson.beijingJson(s);
                listA = list.get(0).articles;
                if (list.get(0).gallery != null) {
                    listG = list.get(0).gallery;
                } else {
                    listG = null;
                }
                if (listG != null) {
                    adapter.setNotifyList(listA);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listA.get(i - 1).weburl);
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter.setNotifyList(listA);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listA.get(i).weburl);
                            startActivity(intent);
                        }
                    });
                }
                springView.onFinishFreshAndLoad();
            }
        });
    }

    //加载头布局图片方法
    public void showHeaderLayout() {
        //加载网络图片
        convenientBanner.setPages(new CBViewHolderCreator<LocNetworkImageHolderView>() {
            public LocNetworkImageHolderView createHolder() {
                return new LocNetworkImageHolderView();
            }
        }, listG)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        intent = new Intent(getActivity(), WebUrlActivity.class);
                        intent.putExtra("weburl", listG.get(position).weburl);
                        startActivity(intent);
                    }
                })
                //设置小圆点
                .setPageIndicator(new int[]{R.drawable.ic_page_bac, R.drawable.ic_page_focused})
                //设置小圆点居右
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        if (listG != null) {
            //开始自动翻页
            convenientBanner.startTurning(5000);
        }
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        if (listG != null) {
            //停止翻页
            convenientBanner.stopTurning();
        }
    }
}
