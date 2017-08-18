package com.hikvision.ga.hephaestus.site.logger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.repository.OperationLogRepository;
import com.hikvision.ga.hephaestus.site.logger.service.OperationLogService;

@Service
public class OperationLogServiceImpl implements OperationLogService {
  
  @Autowired
  OperationLogRepository operationLogRepository;
  
  @Override
  public OperationLog save(OperationLog operationLog) {
    return operationLogRepository.save(operationLog);
  }

  @Override
  public List<OperationLog> save(List<OperationLog> operationLogs) {
    return operationLogRepository.save(operationLogs);
  }

}