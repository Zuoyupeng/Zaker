package com.zuoyupeng.zaker.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "zaker")//表名
public class SQLIteTable {

    @Column(name = "id",isId = true,autoGen = true)//主键
    private int id;

    @Column(name = "titleNews")//表中的字段
    private String titleNews;

    @Column(name = "api_url")
    private String api_url;

    @Column(name = "block_color")
    private String block_color;

    @Column(name = "pic")
    private String pic;

    @Column(name = "titleCha")
    private String titleCha;

    public String getTitleCha() {
        return titleCha;
    }

    public void setTitleCha(String titleCha) {
        this.titleCha = titleCha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }

    public String getBlock_color() {
        return block_color;
    }

    public void setBlock_color(String block_color) {
        this.block_color = block_color;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
