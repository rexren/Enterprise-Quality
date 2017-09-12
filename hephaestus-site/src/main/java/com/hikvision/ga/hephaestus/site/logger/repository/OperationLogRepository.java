package com.hikvision.ga.hephaestus.site.logger.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;

/**
 * 日志Repository
 * @author langyicong
 *
 */
@NoRepositoryBean
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

  /**
   * 按照起止时间query查询日志
   * @param start 查询的起始时间
   * @param end 查询的结束时间
   * @return 日志列表
   */
  List<OperationLog> findLogByTimeRange(Date start, Date end);

}