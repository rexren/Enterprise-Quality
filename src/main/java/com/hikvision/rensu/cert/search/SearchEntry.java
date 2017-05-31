package com.hikvision.rensu.cert.search;

import io.searchbox.annotations.JestId;

/**
 * 这个类用于全局模糊搜索匹配使用
 * 需要将各类检索的材料，先转换为这个结构，然后index
 * Created by rensu on 2017/5/30.
 */
public class SearchEntry {

    @JestId
    private Long id;

    private String title;

    private String caseName;

    private String description;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
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
}
