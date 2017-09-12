package com.hikvision.ga.hephaestus.site.logger;

/**
 * 日志构建器
 * @author langyicong
 *
 */
public class OperationLogBuilder {

  public static Builder build() {
    return new Builder();
  }

  public static class Builder {

    /**
     * Get operationLog from ThreadLocal
     */
    private OperationLog operationLog = OperationLogHolder.getOperationLog();

    /**
     * 重设操作用户的id
     * @param userId 操作用户的id
     * @return OperationLog Builder
     */
    public Builder resetUserId(String userId) {
      operationLog.setUserId(userId);
      return this;
    }

    /**
     * 重设用户名（维护人）
     * @param userName 用户名（维护人）
     * @return OperationLog Builder
     */
    public Builder resetUserName(String userName) {
      operationLog.setUserName(userName);
      return this;
    }

    /**
     * 重设ip
     * @param ip 操作用户所在的IP
     * @return OperationLog Builder
     */
    public Builder resetIp(String ip) {
      operationLog.setIp(ip);
      return this;
    }

    /**
     * 重设操作用户使用的客户端类型
     * @param ua 操作用户使用的客户端类型
     * @return OperationLog Builder
     */
    public Builder resetUserAgent(String ua) {
      operationLog.setUserAgent(ua);
      return this;
    }

    /**
     * 操作业务模块
     * @param businessType 操作业务模块，INSPECTION, CCC, COPYRIGHT等
     * @return OperationLog Builder
     */
    public Builder businessType(String businessType) {
      operationLog.setBusinessType(businessType);
      return this;
    }
    
    /**
     * 设置操作对象ID(独立字段)
     * @param operateObjectId 操作对象ID
     * @return OperationLog Builder
     */
    public Builder operateObjectId(String operateObjectId){
      operationLog.setOperateObjectId(operateObjectId);
      return this;
    }

    /**
     * 设置操作对象其他字段名称
     * @param operateObjectKeys 操作对象编号(多个用 “,” 隔开)
     * @return OperationLog Builder
     */
    public Builder operateObjectKeys(String operateObjectKeys) {
      operationLog.setOperateObjectKeys(operateObjectKeys);
      return this;
    }

    /**
     * 设置操作对象其他字段内容（值）
     * @param operateObjectValues 操作对象名称(多个用 “,” 隔开)
     * @return OperationLog Builder
     */
    public Builder operateObjectValues(String operateObjectValues) {
      operationLog.setOperateObjectValues(operateObjectValues);
      return this;
    }

    /**
     * 操作类型，登陆、查询、新增、修改、删除等
     * @param act
     * @return OperationLog Builder
     */
    public Builder act(String act) {
      operationLog.setAct(act);
      return this;
    }

    /**
     * 操作的结果，1：成功，0：失败
     * @param operateResult 
     * @return OperationLog Builder
     */
    public Builder operateResult(Integer operateResult) {
      operationLog.setOperateResult(operateResult);
      return this;
    }

    /**
     * 失败时的错误码
     * @param errorCode
     * @return OperationLog Builder
     */
    public Builder errorCode(String errorCode) {
      operationLog.setErrorCode(errorCode);
      return this;
    }

    /**
     * 具体的操作日志内容(如果不填写则默认为拦截的URL)
     * @param content
     * @return OperationLog Builder
     */
    public Builder content(String content) {
      operationLog.setContent(content);
      return this;
    }

    /**
     * 真正记录（保存）日志
     */
    public void log() {
      OperationLogHolder.setOperationLog(operationLog);
    }
  }
}
