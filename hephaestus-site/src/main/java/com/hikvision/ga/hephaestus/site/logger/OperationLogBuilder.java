package com.hikvision.ga.hephaestus.site.logger;

public class OperationLogBuilder {

  public static Builder build() {
    return new Builder();
  }

  public static class Builder {

    //Get operationLog from ThreadLocal
    private OperationLog operationLog = OperationLogHolder.getOperationLog();

    public Builder resetUserId(String userId) {
      operationLog.setUserId(userId);
      return this;
    }

    public Builder resetUserName(String userName) {
      operationLog.setUserName(userName);
      return this;
    }

    public Builder resetIp(String ip) {
      operationLog.setIp(ip);
      return this;
    }

    public Builder resetUserAgent(String ua) {
      operationLog.setUserAgent(ua);
      return this;
    }

    public Builder businessType(String businessType) {
      operationLog.setBusinessType(businessType);
      return this;
    }

    public Builder operateObjectKeys(String operateObjectKeys) {
      operationLog.setOperateObjectKeys(operateObjectKeys);
      return this;
    }

    public Builder operateObjectValues(String operateObjectValues) {
      operationLog.setOperateObjectValues(operateObjectValues);
      return this;
    }

    public Builder act(String act) {
      operationLog.setAct(act);
      return this;
    }

    public Builder operateResult(Integer operateResult) {
      operationLog.setOperateResult(operateResult);
      return this;
    }

    public Builder errorCode(String errorCode) {
      operationLog.setErrorCode(errorCode);
      return this;
    }

    public Builder content(String content) {
      operationLog.setContent(content);
      return this;
    }

    public void log() {
      OperationLogHolder.setOperationLog(operationLog);
    }
  }
}
