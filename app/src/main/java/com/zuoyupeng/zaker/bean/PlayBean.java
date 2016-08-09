package com.zuoyupeng.zaker.bean;

import java.util.List;

public class PlayBean {

    public List<ColumnsItem> columns;
    public String next_url;//上一期地址
    public List<PromoteItem> promote;

    public class ColumnsItem {
        public List<Items> items;
    }

    public class PromoteItem{
        public String promotion_img;//头布局图片
        public String title;//头布局标题
        public String weburl;//头布局地址
        public String url;

    }

    public class Items{
        public String title;//标题
        public String content;//描述
        public String url;//图片
        public String weburl;//网址
    }
}
