package com.hikvision.rensu.cert.support;

/**
 * Created by rensu on 2017/5/28.
 */
public class PageResult {
    private String code;
    private String status;

    public PageResult(String code) {
        this.code = code;
    }

    public PageResult() {
    }

    public PageResult(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
