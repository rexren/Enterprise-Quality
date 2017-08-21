package com.hikvision.ga.hephaestus.site.logger.service;

import java.util.List;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;

/**
 * @author langyicong
 *
 */
public interface OperationLogService {


  /**
   * 保存日志
   */
  OperationLog save(OperationLog operationLog);

  /**
   * 批量保存日志
   */
  List<OperationLog> save(List<OperationLog> operationLogDOS);
  
  /**
   * TODO 读取日志
   */
  

}
