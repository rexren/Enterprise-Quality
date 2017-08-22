package com.hikvision.hepaestus.common.support;

/**
 * Created by rensu on 2017/5/28.
 */
public class BaseResult {
    private String code;
    private String msg;
    
    public BaseResult() {
    }

    public BaseResult(String code) {
        this.code = code;
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
