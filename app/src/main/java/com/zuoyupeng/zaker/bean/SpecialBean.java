package com.zuoyupeng.zaker.bean;

import java.util.List;

/**
 * Created by zuo on 2016/8/4.
 */
public class SpecialBean {

    public List<ListItem> list;
    public String next_url;

    public class ListItem{
        public List<TopicItem> topic_list;
    }

    public class TopicItem{
        public List<ArticleIt> article;
        public List<EntranceItem> entrance;
        public List<GalleryItem> gallery;
    }

    public class ArticleIt{
        public String title;//标题
        public String auther_name;//作者
        public String weburl;//网址
    }

    public class EntranceItem{
        public String block_title;//标题
        public String api_url;//标题
    }

    public class GalleryItem{
        public String title;//标题
        public String auther_name;//作者
        public String weburl;//网址
        public String promotion_img;//网址
    }
}
