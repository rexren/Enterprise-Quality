package com.hikvision.ga.hephaestus.site.logger.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikvision.ga.hephaestus.cert.service.TypeInspectionService;
import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.repository.OperationLogRepository;
import com.hikvision.ga.hephaestus.site.logger.service.OperationLogService;

/**
 * 日志服务的实现
 * @author langyicong
 *
 */
@Service
@Transactional
public class OperationLogServiceImpl implements OperationLogService {

  private final static Logger logger = LoggerFactory.getLogger(TypeInspectionService.class);

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

  @Override
  public Page<OperationLog> findLogByTimeRange(int pn, int ps, String sortBy, int dir, Date start,
      Date end) throws Exception{
    Page<OperationLog> p = null;
    Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
    if(start.after(end)){
      throw new Exception("时间先后有误");
    }
    Pageable page = new PageRequest(pn, ps, new Sort(d, sortBy, "id"));
    try {
      List<OperationLog> operationLogList = operationLogRepository.findLogByTimeRange(start, end);
      p = new PageImpl<OperationLog>(operationLogList, page, operationLogList.size());
    } catch (Exception e) {
      logger.error("", e);
      throw e;
    }
    return p;
  } 

}