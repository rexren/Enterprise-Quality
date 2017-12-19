package com.hikvision.ga.hephaestus.common.support;

import com.hikvision.ga.hephaestus.common.constant.RetStatus;

public class AjaxResult<T> extends BaseResult {

  private T data;

  public AjaxResult() {
    super();
  }

  public AjaxResult(T data) {
    super();
    this.data = data;
  }

  public AjaxResult(String code, String msg, T data) {
    super(code, msg);
    this.data = data;
  }
  
  public static <T> AjaxResult<T> success(T data) {
    return new AjaxResult<T>(RetStatus.SUCCESS.getCode(), RetStatus.SUCCESS.getInfo(), data);
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
