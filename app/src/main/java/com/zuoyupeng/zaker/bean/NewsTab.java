package com.zuoyupeng.zaker.bean;

import java.util.List;

/**
 * Created by zuo on 2016/7/25.
 */
public class NewsTab {

    public String titleCha;//外层标题
    public String list_icon;//外层图片地址
    public List<SubTab> list;

    public class SubTab{
        public String api_url;//数据地址
        public String block_color;//主页图片颜色
        public String pic;//内层图标=主页面GridView图片
        public String titleNews;//内层标题=主页面标题
        public int block_bg_key;
        public int pk;
        public String block_title;
        public String large_pic;//主页面GridView图片
    }
}
