package com.zuoyupeng.zaker.bean;

import java.util.List;

public class SpBean {

    public List<ArticlesI> articles;
    public String bgimage_url;
    public String title;

    public class ArticlesI{
        public String auther_name;
        public String title;
        public String weburl;
        public List<MediaI> media;
    }

    public class MediaI{
        public String url;
    }
}
