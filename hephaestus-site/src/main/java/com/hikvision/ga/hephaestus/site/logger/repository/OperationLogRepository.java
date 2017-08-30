package com.hikvision.ga.hephaestus.site.logger.repository;

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

 
}