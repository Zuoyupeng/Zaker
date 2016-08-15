package com.zuoyupeng.zaker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.Target;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.dao.SQLIteTable;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

public class MyRecyclerViewHolder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private View mHeaderView;

    List<SQLIteTable> list;
    Context context;
    DbManager dbManager;

    public MyRecyclerViewHolder(List<SQLIteTable> list, Context context,DbManager dbManager) {
        this.list = list;
        this.context = context;
        this.dbManager = dbManager;
    }

    /**
     * 定义一个接口，实现方法的回掉
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickLitener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickLitener = onItemClickListener;
    }

    /**
     * 引入布局
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ItemHolder viewHolder = new ItemHolder(View.inflate(context,R.layout.recycler1_item,null));
            return viewHolder;
        } else if (viewType == TYPE_HEADER) {
            View view = mHeaderView;
            return new HeaderHolder(view);
        }
        return null;
    }

    /**
     * 给控件赋值
     */
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            //给tv,iv赋值
            ((ItemHolder) holder).tv.setText(getItem(position).getTitleNews());
            Glide.with(context).load(getItem(position).getPic()).into(((ItemHolder) holder).iv);
            int mColor = Color.parseColor(getItem(position).getBlock_color());
            ((ItemHolder) holder).ivB.setBackgroundColor(mColor);//设置背景颜色
            if (onItemClickLitener != null) {
                //短按点击事件
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemClick(holder.itemView, pos-1);
                    }
                });
                //长按的点击事件
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickLitener.onItemLongClick(holder.itemView, pos-1);
                        return false;
                    }
                });
            }
        }
    }

    /**
     * 数据集合的长度
     */
    public int getItemCount() {
        return list.size();
    }

    /**
     * 头布局和主布局的类型判断
     */
    public int getItemViewType(int position) {
        if (position < getHeaderViewSize()) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    //头布局的Size
    private int getHeaderViewSize() {
        return mHeaderView == null ? 0 : 1;
    }

    //item布局的Size
    private SQLIteTable getItem(int position) {
        return list.get(position);
    }

    //添加0位头布局
    public void addHeader(View header) {
        mHeaderView = header;
        notifyItemInserted(0);
    }

    //刷新数据
    public void notifyData(List<SQLIteTable> data){
        this.list = data;
        notifyItemChanged(list.size());
    }

    //添加list数据
    public void addData(SQLIteTable data) {
        list.add(data);
        notifyItemInserted(getHeaderViewSize() + list.size() - 1);
    }

    //删除list数据
    public void deleteData(int i,int position){
        list.remove(position+1);
        delete(i);
        notifyItemRemoved(position+1);
    }

    //实例化控件
    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv,ivB;
        public ItemHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.recycler_item_iv);
            ivB = (ImageView) itemView.findViewById(R.id.recycler_fm_iv);
            tv = (TextView) itemView.findViewById(R.id.recycler_item_tv);
        }
    }

    //实例化头布局
    public class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    //删除数据库内容
    private void delete(int i){
        try {
            dbManager.deleteById(SQLIteTable.class,i);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
