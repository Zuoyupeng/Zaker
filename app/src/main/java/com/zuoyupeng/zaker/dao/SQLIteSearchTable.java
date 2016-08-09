package com.zuoyupeng.zaker.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "zakerSearch")//表名
public class SQLIteSearchTable {

    @Column(name = "ii",isId = true,autoGen = true)//主键
    private int ii;

    public int getIi() {
        return ii;
    }

    public void setIi(int ii) {
        this.ii = ii;
    }

    @Column(name = "titleCha")
    private String titleCha;

    @Column(name = "list_icon")
    private String list_icon;

    public String getTitleChannel() {
        return titleCha;
    }

    public void setTitleChannel(String titleCha) {
        this.titleCha = titleCha;
    }

    public String getList_icon() {
        return list_icon;
    }

    public void setList_icon(String list_icon) {
        this.list_icon = list_icon;
    }

}
