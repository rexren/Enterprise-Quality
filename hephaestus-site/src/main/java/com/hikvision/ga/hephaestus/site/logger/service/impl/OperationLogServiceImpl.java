package com.hikvision.ga.hephaestus.site.logger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  @Override
  public List<OperationLog> findListAll() {
    return operationLogRepository.findAll();
  }

  @Override
  public Page<OperationLog> findLogByPage(int pn, int ps, String sortBy, int dir) {
    Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
    Pageable page = new PageRequest(pn, ps, new Sort(d, sortBy, "id")); 
    return operationLogRepository.findAll(page);
  } 

}