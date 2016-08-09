package com.zuoyupeng.zaker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
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
import com.zuoyupeng.zaker.adapter.PlayAdapter;
import com.zuoyupeng.zaker.bean.PlayBean;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;
import com.zuoyupeng.zaker.view.PlayNetworkImageHolderView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PlayFragment extends BaseFragment {

    ConvenientBanner convenientBanner;//轮播图
    ListView lv;
    List<PlayBean> list = new ArrayList<>();
    List<PlayBean.Items> listC = new ArrayList<>();
    List<PlayBean.PromoteItem> listP = new ArrayList<>();
    PlayAdapter adapter;
    SpringView springView;
    int count = 1;
    View view;
    ImageView iv;
    ProgressWheel progressWheel;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressWheel.stopSpinning();
            iv.setVisibility(View.VISIBLE);
            onPlayNet(0);
            return false;
        }
    });

    public View initView() {
        view = View.inflate(getActivity(), R.layout.fragment_play, null);
        lv = (ListView) view.findViewById(R.id.fragment_play_lv);
        springView = (SpringView) view.findViewById(R.id.play_sv);
        iv = (ImageView) view.findViewById(R.id.play_iv_error);
        //引入轮播图布局
        convenientBanner = (ConvenientBanner) LayoutInflater.from(getActivity())
                .inflate(R.layout.adapter_header_play, null);
        convenientBanner.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 500));
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
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
        onPlayNet(0);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onPlayNet(1);
            }

            @Override
            public void onLoadmore() {
                onPlayNetMore();
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
    }

    public void onPlayNetMore() {
        OkHttpUtils.get().url(NetWorkUtils.NET_PLAY).build().execute(new StringCallback() {

            public void onError(Call call, Exception e, int i) {
                Log.i("PlayFragment", "error");
            }

            public void onResponse(String s, int i) {
                list = NetJson.playJson(s);
                count++;
                if (count < 5) {
                    listC.addAll(list.get(0).columns.get(count).items);
                }
                adapter.setNotifyList(listC);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                        intent.putExtra("weburl", listC.get(i - 1).weburl);
                        startActivity(intent);
                    }
                });
                springView.onFinishFreshAndLoad();
            }
        });
    }

    public void onPlayNet(final int type) {
        OkHttpUtils.get().url(NetWorkUtils.NET_PLAY).build().execute(new StringCallback() {

            public void onError(Call call, Exception e, int i) {
                LogUtils.i("PlayFragment", "error");
                handler.sendEmptyMessageDelayed(0, 3000);
            }

            public void onResponse(String s, int i) {
                list = NetJson.playJson(s);
                if (type == 0) {
                    listP = list.get(0).promote;
                    showHeaderLayout();
                    listC = list.get(0).columns.get(0).items;
                    adapter = new PlayAdapter(listC, getActivity());
                    lv.setAdapter(adapter);
                    lv.addHeaderView(convenientBanner);
                    adapter.setNotifyList(listC);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listC.get(i - 1).weburl);
                            startActivity(intent);
                        }
                    });
                } else if (type == 1) {
                    listC = list.get(0).columns.get(0).items;
                    adapter.setNotifyList(listC);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                            intent.putExtra("weburl", listC.get(i - 1).weburl);
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

    //加载头布局图片方法
    public void showHeaderLayout() {
        //加载网络图片
        convenientBanner.setPages(new CBViewHolderCreator<PlayNetworkImageHolderView>() {
            public PlayNetworkImageHolderView createHolder() {
                return new PlayNetworkImageHolderView();
            }
        }, listP)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                        if(listP.get(position).url != null){
                            intent.putExtra("weburl", listP.get(position).url);
                        }else {
                            intent.putExtra("weburl", listP.get(position).weburl);
                        }
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
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

}
