package com.zuoyupeng.zaker.bean;

import java.util.List;

public class Commbean {

    public List<PostsItem> posts;
    public String next_url;
    public String title;

    public class PostsItem{
        public String icon;//图标
        public String name;//作者名字
        public String content;//新闻名字
        public String weburl;//网址
        public int hot_num;//热度
        public List<MediasItem> medias;
    }

    public class MediasItem{
        public String url;//图片
    }
}
