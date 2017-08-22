package com.hikvision.ga.hephaestus.site.logger.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.repository.OperationLogRepository;

@Repository
public class OperationLogRepositoryImpl extends SimpleJpaRepository<OperationLog, Long>
    implements OperationLogRepository {

  public OperationLogRepositoryImpl(EntityManager em) {
    super(OperationLog.class, em);
  }

}
