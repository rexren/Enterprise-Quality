package com.hikvision.rensu.cert.support;

/**
 * Created by rensu on 2017/5/28.
 */
public class PageResult {
    private String code;
    private String errorMsg;

    public PageResult(String code) {
        this.code = code;
    }

    public PageResult() {
    }

    public PageResult(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
