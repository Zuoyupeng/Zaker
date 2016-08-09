package com.zuoyupeng.zaker.bean;

import java.util.List;

public class HotBean{

    public String auther_name;
    public String date;
    public String title;
    public String weburl;
    public String item_type;
    public List<Medias> thumbnail_medias;

    public class Medias {
        public String url;
    }
}
