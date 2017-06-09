package com.hikvision.rensu.cert.search;

import io.searchbox.annotations.JestId;

/**
 * 这个类用于全局模糊搜索匹配使用
 * 需要将各类检索的材料，先转换为这个结构，然后index
 * Created by rensu on 2017/5/30.
 */
public class IndexEntry {

    @JestId
    private String id;

    private String title;

    private String description;

    private String rowContent;

    private String remark;

    /**
     * Index type
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRowContent() {
        return rowContent;
    }

    public void setRowContent(String rowContent) {
        this.rowContent = rowContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
