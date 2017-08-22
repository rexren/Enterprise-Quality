package com.hikvision.ga.hephaestus.site.logger;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OperationLog implements Serializable {

  private static final long serialVersionUID = 2041786434311079170L;

  /**
   * 自增Id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 日志的创建时间，由日志提供方提供
   */
  private Date createTime;

  /**
   * 操作用户的id
   */
  private String userId;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 操作用户所在的IP
   */
  private String ip;
  
  /**
   * 操作用户使用的客户端类型
   */
  private String userAgent;

  /**
   * 操作业务模块，INSPECTION, CCC, COPYRIGHT等
   */
  private String businessType;

  /**
   * 操作对象编号(多个用“,”隔开)
   */
  private String operateObjectKeys;

  /**
   * 操作对象名称(多个用“,”隔开)
   */
  private String operateObjectValues;

  /**
   * 操作类型，登陆、查询、新增、修改、删除、预览等
   */
  private String act;

  /**
   * 操作的结果，1：成功，0：失败
   */
  private Integer operateResult;

  /**
   * 失败时错误码必填
   */
  private String errorCode;

  /**
   * 具体的操作日志内容(如果不填写则默认为拦截的URL)
   */
  private String content;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getOperateObjectKeys() {
    return operateObjectKeys;
  }

  public void setOperateObjectKeys(String operateObjectKeys) {
    this.operateObjectKeys = operateObjectKeys;
  }

  public String getOperateObjectValues() {
    return operateObjectValues;
  }

  public void setOperateObjectValues(String operateObjectValues) {
    this.operateObjectValues = operateObjectValues;
  }

  public String getAct() {
    return act;
  }

  public void setAct(String act) {
    this.act = act;
  }

  public Integer getOperateResult() {
    return operateResult;
  }

  public void setOperateResult(Integer operateResult) {
    this.operateResult = operateResult;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  @Override
  public String toString() {
    return "OperationLog [id=" + id + ", createTime=" + createTime + ", userId=" + userId
        + ", userName=" + userName + ", ip=" + ip + ", userAgent=" + userAgent
        + ", businessType=" + businessType + ", operateObjectKeys=" + operateObjectKeys
        + ", operateObjectValues=" + operateObjectValues + ", act=" + act + ", operateResult="
        + operateResult + ", errorCode=" + errorCode + ", content=" + content + "]";
  }

}
