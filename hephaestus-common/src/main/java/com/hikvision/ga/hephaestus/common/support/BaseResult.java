package com.hikvision.ga.hephaestus.common.support;

import java.io.Serializable;

import com.hikvision.ga.hephaestus.common.constant.RetStatus;

/**
 * Created by rensu on 2017/5/28.
 */
public class BaseResult implements Serializable {

  private static final long serialVersionUID = 1L;

  private String code;

  private String msg;
  /**
   * 无参构造函数
   */
  public BaseResult() {}

  /**
   * @param code 处理结果业务代码
   * @param msg 处理结果业务信息
   */
  public BaseResult(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }
  
  /**
   * 成功
   * @return BaseResult
   */
  public static BaseResult success() {
    return new BaseResult(RetStatus.SUCCESS.getCode(), RetStatus.SUCCESS.getInfo());
  }
  
  /**
   * 失败
   * @param code 处理结果业务代码
   * @param msg 处理结果业务信息
   * @return BaseResult
   */
  public static BaseResult fail(String code, String msg) {
    return new BaseResult(code, msg);
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
