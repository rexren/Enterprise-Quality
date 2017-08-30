package com.hikvision.ga.hephaestus.site.logger.service;

import java.util.Date;
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
   * @param operationLog 待保存的日志对象
   * @return 保存成功的日志对象
   */
  OperationLog save(OperationLog operationLog);

  /**
   * 批量保存日志
   * @param operationLogs 待保存的日志对象列表
   * @return 保持成功的日志对象列表
   */
  List<OperationLog> save(List<OperationLog> operationLogs);
  
  /**
   * 获取日志列表
   * @return 所有的日志对象列表
   */
  List<OperationLog> findListAll();
  
  /**
   * 获取日志列表页
   * @param pn 页码
   * @param ps 页容量
   * @param sortBy 排序字段 
   * @param dir 升序还是降序
   * @return 日志列表页
   */
  Page<OperationLog> findLogByPage(int pn, int ps, String sortBy, int dir);
  
  /**
   * 按照时间段获取日志列表页
   * @param pn 页码
   * @param ps 页容量
   * @param sortBy 排序字段 
   * @param dir 升序还是降序
   * @param start 查询的起始时间
   * @param end 查询的结束时间
   * @return 日志列表页
   */
  Page<OperationLog> findLogByTimeRange(int pn, int ps, String sortBy, int dir, Date start, Date end);

}
