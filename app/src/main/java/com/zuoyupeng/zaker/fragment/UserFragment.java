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

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.CommunityActivity;
import com.zuoyupeng.zaker.adapter.CommunityAdapter;
import com.zuoyupeng.zaker.bean.CommunityBean;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import java.util.List;

import okhttp3.Call;

public class UserFragment extends BaseFragment {

    ListView lv;
    List<CommunityBean> list;
    CommunityAdapter adapter;
    SpringView springView;
    ProgressWheel progressWheel;
    ImageView iv;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressWheel.stopSpinning();
            iv.setVisibility(View.VISIBLE);
            onCommunityNet();
            return false;
        }
    });


    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_user, null);
        lv = (ListView) view.findViewById(R.id.fragment_user_lv);
        springView = (SpringView) view.findViewById(R.id.user_sv);
        iv = (ImageView) view.findViewById(R.id.user_iv_error);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        return view;
    }

    public void setData() {
        progressWheel.setProgress(0.0f);
        progressWheel.setBarColor(Color.BLUE);
        progressWheel.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float v) {
                if(v== 0){
                    progressWheel.setProgress(1.0f);
                }else if(v==1){
                    progressWheel.setProgress(0.0f);
                }
            }
        });
        onCommunityNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onCommunityNet();
            }

            @Override
            public void onLoadmore() {
                onCommunityNetMore();
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
    }

    public void onCommunityNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_SHEQU).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Log.i("UserFragment", "error");
                        handler.sendEmptyMessageDelayed(0,3000);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        list = NetJson.communityJson(s);
                        Log.i("UserFragment", "success");
                        adapter = new CommunityAdapter(list, getActivity());
                        lv.setAdapter(adapter);
                        adapter.setNotifyList(list);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                                intent.putExtra("api_url",list.get(i).api_url);
                                startActivity(intent);
                            }
                        });
                        springView.onFinishFreshAndLoad();
                        progressWheel.stopSpinning();
                        iv.setVisibility(View.GONE);
                    }
                });
    }

    public void onCommunityNetMore(){
        OkHttpUtils.get().url(NetWorkUtils.NET_SHEQU_MORE).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Log.i("UserFragment", "error");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        List<CommunityBean> list1 = NetJson.communityJson(s);
                        list.addAll(list1);
                        Log.i("UserFragment", "success");
                        adapter.setNotifyList(list);
                        springView.onFinishFreshAndLoad();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                                intent.putExtra("api_url",list.get(i).api_url);
                                startActivity(intent);
                            }
                        });
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
