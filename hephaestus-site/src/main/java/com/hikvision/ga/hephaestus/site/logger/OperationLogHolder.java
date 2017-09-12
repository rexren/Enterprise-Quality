package com.hikvision.ga.hephaestus.site.logger;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;

public class OperationLogHolder {

  private static final ThreadLocal<OperationLog> operationLogDTOThreadLocal = new ThreadLocal<>();

  public static OperationLog getOperationLog() {
    return operationLogDTOThreadLocal.get();
  }

  public static void setOperationLog(final OperationLog operationLog) {
    operationLogDTOThreadLocal.set(operationLog);
  }

  public static void clear() {
    operationLogDTOThreadLocal.remove();
  }
}
