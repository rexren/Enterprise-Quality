package com.hikvision.ga.hephaestus.site.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.cert.service.CccPageService;
import com.hikvision.ga.hephaestus.common.constant.RetStatus;
import com.hikvision.ga.hephaestus.common.support.ListContent;
import com.hikvision.ga.hephaestus.common.support.ListResult;
import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.service.OperationLogService;

/**
 * 更新记录/操作日志接口
 * 
 * @author langyicong
 *
 */
@Controller
@RequestMapping("/log")
public class OperationLogController {

  private static final Logger logger = LoggerFactory.getLogger(CccPageService.class);

  @Autowired
  OperationLogService operationLogService;

  /**
   * 获取操作日志列表页
   *
   * @param pageNum 页码 默认为第一页
   * @param pageSize 页大小 默认20条/页
   * @param startTime 起始时间
   * @param endTime 结束时间
   * @return 包含操作日志数据对象的返回对象
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult getListByPage(Integer pageNum, Integer pageSize, Long startTime, Long endTime) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = 0; // 默认为降序
    String sortBy = "createTime"; // 默认按照操作时间和id倒序

    ListResult res = new ListResult();
    /*如果选择了起止时间*/
    if (startTime != null && endTime != null) {
      try {
        Date start = new Date(startTime);
        Date end = new Date(endTime);
        if(start.after(end)){
          throw new Exception("时间先后有误");
        }
        Page<OperationLog> p = operationLogService.findLogByTimeRange(pn, ps, sortBy, dir, start, end);
        if (null != p) {
          res.setListContent(
              new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(), p.getContent()));
        }
        res.setCode(RetStatus.SUCCESS.getCode());
        res.setMsg(RetStatus.SUCCESS.getInfo());
      } catch (Exception e) {
        logger.error("", e);
        res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
        res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      }
      return res;
    }
    //如果起始和结束时间不符合要求
    try {
      Page<OperationLog> p = operationLogService.findLogByPage(pn, ps, sortBy, dir);
      if (null != p) {
        res.setListContent(
            new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(), p.getContent()));
      }
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
    }
    return res;
  }
}
