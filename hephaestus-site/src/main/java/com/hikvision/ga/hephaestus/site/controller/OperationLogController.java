package com.hikvision.ga.hephaestus.site.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.cert.service.CccPageService;
import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.OperationLogIgnore;
import com.hikvision.ga.hephaestus.site.logger.service.OperationLogService;
import com.hikvision.hepaestus.common.constant.RetStatus;
import com.hikvision.hepaestus.common.support.ListContent;
import com.hikvision.hepaestus.common.support.ListResult;

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
   * @return 包含操作日志数据对象的返回对象
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult getListByPage(Integer pageNum, Integer pageSize) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = 0; // 默认为降序
    String sortBy = "createTime"; // 默认按照软操作时间和id倒序

    ListResult res = new ListResult();
    try {
      Page<OperationLog> p = operationLogService.findLogByPage(pn, ps, sortBy, dir);
      if (null != p) {
        res.setListContent(new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(), p.getContent()));
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
