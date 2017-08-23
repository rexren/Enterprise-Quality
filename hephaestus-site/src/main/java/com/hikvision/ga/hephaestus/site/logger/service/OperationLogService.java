package com.hikvision.ga.hephaestus.site.logger.service;

import java.util.List;

import org.springframework.data.domain.Page;
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
   * @return 保持成功的日志结果
   */
  List<OperationLog> save(List<OperationLog> operationLogDOS);
  
  /**
   * 读取日志列表
   */
  List<OperationLog> findListAll();
  
  /**
   * 读取日志列表页
   */
  Page<OperationLog> findLogByPage(int pn, int ps, String sortBy, int dir);

}
