package com.zuoyupeng.zaker.bean;

import java.util.List;

public class BeijingBean {

    public List<Articles> articles;
    public List<Gallery> gallery;

    public class Articles{
        public String auther_name;//来源
        public String title;//标题
        public String weburl;//地址
        public String thumbnail_pic;//图片
    }

    public class Gallery{
        public String title;
        public String weburl;
        public String promotion_img;
    }
}
