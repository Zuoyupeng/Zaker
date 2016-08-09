package com.zuoyupeng.zaker.bean;

import java.util.List;

/**
 * Created by zuo on 2016/7/22.
 */
public class HeaderPicTab {

    public Data data;

    public class Data{
        public List<News> list;
    }

    public static class News{
        public String promotion_img;
        public String title;
        public Topic topic;
        public Block block_info;
        public Article article;
        public Post post;
    }

    public class Topic{
        public String api_url;
        public String title;
    }
    public class Block{
        public String api_url;
        public String title;
    }
    public class Article{
        public String weburl;
    }

    public class Post{
        public String weburl;
    }
}
