package com.zuoyupeng.zaker.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.activity.ReadActivity;
import com.zuoyupeng.zaker.activity.SpActivity;
import com.zuoyupeng.zaker.activity.WebUrlActivity;
import com.zuoyupeng.zaker.adapter.MyRecyclerViewHolder;
import com.zuoyupeng.zaker.bean.HeaderPicTab;
import com.zuoyupeng.zaker.dao.SQLIteTable;
import com.zuoyupeng.zaker.dao.XUtilDb;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;
import com.zuoyupeng.zaker.view.NetworkImageHolderView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SubscribeFragment extends BaseFragment {

    RecyclerView recyclerView;
    List<SQLIteTable> listGridView = new ArrayList<>();
    MyRecyclerViewHolder viewHolder;
    ConvenientBanner convenientBanner;//声明轮播图
    List<HeaderPicTab.News> listHeader;//获取header网络图片和文字
    DbManager dbManager;//数据库
    View view;

    /**
     * 实例化
     */
    public View initView() {
        //引入RecyclerView布局
        view = View.inflate(getActivity(), R.layout.fragment_subscribe, null);
        //数据库
        DbManager.DaoConfig daoConfig = XUtilDb.getDaoConfig();
        dbManager = x.getDb(daoConfig);
        //实例化
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//动画
        //引入轮播图布局
        convenientBanner = (ConvenientBanner) LayoutInflater.from(getActivity())
                .inflate(R.layout.adapter_header, null);
        convenientBanner.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 600));
        //引入GridView布局
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        //设置头布局占整个一行
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (viewHolder.getItemViewType(position) == MyRecyclerViewHolder.TYPE_HEADER) {
                    return layoutManager.getSpanCount();
                }
                return 1;
            }
        });
        layoutManager.setSpanCount(layoutManager.getSpanCount());
        //添加gridView到recyclerView
        recyclerView.setLayoutManager(layoutManager);
        //查找本地数据库，赋值给list
        query();
        //实例化
        viewHolder = new MyRecyclerViewHolder(listGridView, getActivity(), dbManager);
        recyclerView.setAdapter(viewHolder);
        //添加头布局轮播图
        viewHolder.addHeader(convenientBanner);
        return view;
    }

    /**
     * 设置数据
     */
    public void setData() {
        //网络请求并解析Header布局
        onHeaderNet();
        //gridView点击事件
        viewHolder.setOnItemClickListener(new MyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                intent.putExtra("Api_url", listGridView.get(position).getApi_url());
                intent.putExtra("TitleNews", listGridView.get(position).getTitleNews());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                showDialog(position);
            }
        });
    }

    /**
     * 网络请求并解析Header布局
     */
    private void onHeaderNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_ADDRESS).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtils.i("SubscribeFragment", "网络请求失败");
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtils.i("SubscribeFragment", "网络请求成功");
                Gson gson = new Gson();
                HeaderPicTab tab = gson.fromJson(s, HeaderPicTab.class);
                listHeader = tab.data.list;//将解析后的数据赋值给List
                showHeaderLayout();//调用方法
            }
        });
    }

    /**
     * 加载头布局图片方法
     */
    public void showHeaderLayout() {
        //加载网络图片
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, listHeader).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listHeader.get(position).block_info == null && listHeader.get(position).topic
                        == null&&listHeader.get(position).post == null) {
                    Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                    intent.putExtra("weburl", listHeader.get(position).article.weburl);
                    startActivity(intent);
                } else if (listHeader.get(position).block_info == null&&
                        listHeader.get(position).topic != null&&listHeader.get(position).post == null) {
                    Intent intent = new Intent(getActivity(), SpActivity.class);
                    intent.putExtra("api_url", listHeader.get(position).topic.api_url);
                    intent.putExtra("promotion_img", listHeader.get(position).promotion_img);
                    startActivity(intent);
                }else if(listHeader.get(position).post != null&&listHeader.get(position).block_info == null
                        &&listHeader.get(position).topic == null){
                    Intent intent = new Intent(getActivity(), WebUrlActivity.class);
                    intent.putExtra("weburl", listHeader.get(position).post.weburl);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), ReadActivity.class);
                    intent.putExtra("Api_url", listHeader.get(position).block_info.api_url);
                    intent.putExtra("TitleNews", listHeader.get(position).block_info.title);
                    startActivity(intent);
                }
            }
        })
                //设置小圆点
                .setPageIndicator(new int[]{R.drawable.ic_page_bac, R.drawable.ic_page_focused})
                //设置小圆点居右
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
    }

    // 开始自动翻页
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
    }

    // 停止自动翻页
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    /**
     * 查找数据库，得到全部数据
     */
    private void query() {
        try {
            listGridView = dbManager.findAll(SQLIteTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示一个对话框，用来删除主界面磁块
     */
    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("温馨提示");
        builder.setIcon(R.drawable.ding);
        builder.setMessage("您要删除" + listGridView.get(position).getTitleNews() + "吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (position < 5) {
                    Toast.makeText(getActivity(), "至少留点看嘛", Toast.LENGTH_SHORT).show();
                } else {
                    viewHolder.deleteData(listGridView.get(position).getId(), position);
                    Toast.makeText(getActivity(), "我还会回来的!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "谢谢小主不杀之恩", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
