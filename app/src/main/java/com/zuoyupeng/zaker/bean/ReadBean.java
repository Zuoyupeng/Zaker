package com.zuoyupeng.zaker.bean;

import java.util.List;

public class ReadBean {

    public List<PagesItem> pages;
    public String next_url;//下一条地址
    public String block_title;//下一条地址
    public List<ArticlesItem> articles;//新闻地址集合

    public class ArticlesItem {
        public String auther_name;//来源地（新华社）
        public String date;//时间
        public String title;//新闻标题
        public String weburl;//新闻地址Url
        public List<MediaItem> media;//判断是否为空，有些为空
    }

    public class MediaItem {
        public String url;//新闻大图片显示
    }

    public class PagesItem {
        public String bgimage_url;//新闻阅读界面顶部状态栏
    }

}
