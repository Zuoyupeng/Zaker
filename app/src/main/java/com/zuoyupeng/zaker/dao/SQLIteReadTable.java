package com.zuoyupeng.zaker.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "zakerRead")//表名
public class SQLIteReadTable{

    @Column(name = "id",isId = true,autoGen = true)//主键
    private int id;

    @Column(name = "auther_name")//表中的字段
    private String auther_name;

    @Column(name = "title")
    private String title;

    @Column(name = "weburl")
    private String weburl;

    @Column(name = "date")
    private String date;

    @Column(name = "url")
    private String url;

    @Column(name = "bgimage_url")
    private String bgimage_url;

    @Column(name = "block_title")
    private String block_title;

    public String getBlock_title() {
        return block_title;
    }

    public void setBlock_title(String block_title) {
        this.block_title = block_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuther_name() {
        return auther_name;
    }

    public void setAuther_name(String auther_name) {
        this.auther_name = auther_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBgimage_url() {
        return bgimage_url;
    }

    public void setBgimage_url(String bgimage_url) {
        this.bgimage_url = bgimage_url;
    }

}
