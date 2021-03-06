package com.hikvision.ga.hephaestus.site.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.cert.domain.CccPage;
import com.hikvision.ga.hephaestus.cert.service.CccPageService;
import com.hikvision.ga.hephaestus.common.constant.RetStatus;
import com.hikvision.ga.hephaestus.common.support.AjaxResult;
import com.hikvision.ga.hephaestus.common.support.BaseResult;
import com.hikvision.ga.hephaestus.common.support.ListContent;
import com.hikvision.ga.hephaestus.common.support.ListResult;
import com.hikvision.ga.hephaestus.site.cert.constant.BusinessType;
import com.hikvision.ga.hephaestus.site.cert.constant.HikRole;
import com.hikvision.ga.hephaestus.site.cert.constant.OperationAct;
import com.hikvision.ga.hephaestus.site.logger.OperationLogBuilder;
import com.hikvision.ga.hephaestus.site.logger.OperationLogIgnore;
import com.hikvision.ga.hephaestus.site.security.annotation.HikRoleAccess;
import com.hikvision.ga.hephaestus.site.security.service.HikUserService;


/**
 * CCC controller
 * 
 * @author langyicong
 *
 */
@Controller
@RequestMapping("/ccc")
public class CccPageController {
  private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

  private final static String SORTBY_AWARDDATE = "awardDate";

  @Autowired
  private CccPageService cccPageService;

  @Autowired
  private HikUserService hikUserService;

  /**
   * CCC列表页
   *
   * @param pageNum 页码 默认为第一页
   * @param pageSize 页大小 默认20条/页
   * @param sortBy 需要倒序排序的字段，默认为更新时间UpdateAt字段
   * @param direction <=0表示降序，>0为升序，默认为降序
   * @return 包含型检数据对象的返回对象
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult getCCCListByPage(Integer pageNum, Integer pageSize, String sortBy,
      Integer direction) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
    if (StringUtils.isBlank(sortBy)) {
      sortBy = SORTBY_AWARDDATE; // 默认按照颁发日期和id倒序
    }
    ListResult res = new ListResult();
    try {
      Page<CccPage> p = cccPageService.getCCCListByPage(pn, ps, sortBy, dir);
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

  /**
   * 删除单条型检数据（不包括列表）
   *
   * @param id 型检id
   * @return 操作状态
   */
  @HikRoleAccess(roles = HikRole.ADMIN)
  @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
  @ResponseBody
  public BaseResult deleteCopyrightById(Long id) {
    // 操作日志：记录动作和模块
    OperationLogBuilder.build().act(OperationAct.DELETE).businessType(BusinessType.CCC);
    BaseResult res = new BaseResult();
    if (null == id) {
      res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
      res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      // 操作失败
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.PARAM_ILLEGAL.getCode())
          .log();
      return res;
    }
    try {
      // 操作日志：写id
      OperationLogBuilder.build().operateObjectId(Long.toString(id));
      CccPage c = cccPageService.getCccPageById(id);
      // 操作日志：记录信息
      OperationLogBuilder.build().operateObjectKeys("model,productName,docNo")
          .operateObjectValues(c.getModel().toString() + "," + c.getProductName().toString() + ","
              + c.getDocNo().toString());
      cccPageService.deleteCccPageById(id);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作结果：成功
      OperationLogBuilder.build().operateResult(1);
    } catch (IllegalArgumentException e) {
      logger.error("", e);
      res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
      res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.PARAM_ILLEGAL.getCode());
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode());
    }
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 获取CCC单条数据
   * 
   * @param id 查询主键
   * @return CCC单条数据返回结果
   */
  @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
  @ResponseBody
  public AjaxResult<CccPage> getCccPage(Long id) {
    AjaxResult<CccPage> res = new AjaxResult<CccPage>();
    if (null == id) {
      res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
      res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
    } else {
      try {
        CccPage ccc = cccPageService.getCccPageById(id);
        if (ccc != null) {
          res.setData(ccc);
          res.setCode(RetStatus.SUCCESS.getCode());
          res.setMsg(RetStatus.SUCCESS.getInfo());
        } else {
          res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
          res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
        }
      } catch (Exception e) {
        logger.error("", e);
        res.setCode(RetStatus.SYSTEM_ERROR.getCode());
        res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      }
    }
    return res;
  }

  /**
   * 新建保存单条数据
   *
   * @param request 3C编辑页表单内容
   * @return res 返回状态
   * @author langyicong
   */
  @HikRoleAccess(roles = HikRole.ADMIN)
  @RequestMapping(value = "/save.do", method = RequestMethod.POST)
  @ResponseBody
  public BaseResult saveCccPage(HttpServletRequest request) {
    // 操作日志：记录操作模块和动作
    OperationLogBuilder.build().act(OperationAct.ADD).businessType(BusinessType.CCC);
    BaseResult res = new BaseResult();
    /* 表单验证:关键信息docNo不为空 */
    if (StringUtils.isBlank(request.getParameter("docNo"))
        || StringUtils.isBlank(request.getParameter("model"))
        || StringUtils.isBlank(request.getParameter("productName"))
        || StringUtils.isBlank(request.getParameter("awardDate"))
        || StringUtils.isBlank(request.getParameter("expiryDate"))) {
      res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
      res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
      // 操作日志：记录错误信息
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FORM_DATA_MISSING.getCode())
          .log();
      return res;
    }
    /* 表单验证:docNo不能重复 */
    String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
    if (cccPageService.findByDocNo(docNoStripped).size() > 0) {
      res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
      res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.DOCNO_DUPLICATED.getCode())
          .log();
      return res;
    }
    CccPage c = new CccPage();
    try {
      /* 页面参数组装成CccPage实体 */
      c = setCccProperties(request, c);
      cccPageService.saveCccPage(c);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作成功记录日志
      OperationLogBuilder.build()
          .operateObjectKeys("model,productName,docNo").operateObjectValues(c.getModel().toString()
              + "," + c.getProductName().toString() + "," + c.getDocNo().toString())
          .operateResult(1);
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode());
    }
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 更新单条数据
   *
   * @param request CCC编辑页表单内容
   * @return res 返回状态
   * @author langyicong
   */
  @HikRoleAccess(roles = HikRole.ADMIN)
  @RequestMapping(value = "/update.do", method = RequestMethod.POST)
  @ResponseBody
  public BaseResult updateCccPage(HttpServletRequest request) {
    // 操作日志：记录操作模块和动作
    OperationLogBuilder.build().act(OperationAct.UPDATE).businessType(BusinessType.CCC);

    BaseResult res = new BaseResult();
    /* if null id or null entity */
    if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
      res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
      res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
      // 操作日志：记录失败动作
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FORM_DATA_MISSING.getCode())
          .log();
      return res;
    }
    // 操作日志：记录id
    OperationLogBuilder.build().operateObjectId(request.getParameter("id"));
    /* 查询对应的实体，并且将页面数据更新实体内容 */
    Long requestId = NumberUtils.toLong(request.getParameter("id"));
    CccPage c = null;
    try {
      c = cccPageService.getCccPageById(requestId);
      if (null == c) {
        res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
        res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.ITEM_NOT_FOUND.getCode())
            .log();
        return res;
      }
      // 操作日志：记录条目信息
      OperationLogBuilder.build().operateObjectKeys("model,productName,docNo")
          .operateObjectValues(c.getModel().toString() + "," + c.getProductName().toString() + ","
              + c.getDocNo().toString());
      // 编辑后docNo无重复
      String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
      if (cccPageService.findByDocNo(docNoStripped).size() > 0
          && !StringUtils.equals(c.getDocNo(), docNoStripped)) {
        res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
        res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.DOCNO_DUPLICATED.getCode())
            .log();
        return res;
      } ;
      c = setCccProperties(request, c);
      cccPageService.saveCccPage(c);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作结果：成功
      OperationLogBuilder.build()
          .operateObjectKeys("model,productName,docNo").operateObjectValues(c.getModel().toString()
              + "," + c.getProductName().toString() + "," + c.getDocNo().toString())
          .operateResult(1);
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      // 操作日志：记录失败动作
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode())
          .log();
      return res;
    }
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 将HttpServletRequest请求体（页面参数）组装成CccPage实体
   *
   * @param request 页面表单数据
   * @param c 初始的CccPage实体，可为空（新建）或者含id（更新）
   * @return c 转换后的CccPage实体，不包括创建时间
   * @throws Exception
   */
  private CccPage setCccProperties(HttpServletRequest request, CccPage c) throws Exception {
    if (null == c) {
      c = new CccPage();
    }
    /* 新建 */
    if (null == c.getId() || c.getId() < 0) {
      c.setCreateDate(new Date());
    }
    /* setXxxxDate */
    c.setExpiryDate(NumberUtils.toLong(request.getParameter("expiryDate")) > 0
        ? new Date(NumberUtils.toLong(request.getParameter("expiryDate"))) : null);
    c.setAwardDate(NumberUtils.toLong(request.getParameter("awardDate")) > 0
        ? new Date(NumberUtils.toLong(request.getParameter("awardDate"))) : null);
    c.setUpdateDate(new Date());
    /* set other properties */
    c.setDocNo(StringUtils.trim(request.getParameter("docNo")));
    c.setProductName(StringUtils.trim(request.getParameter("productName")));
    c.setModel(StringUtils.trim(request.getParameter("model")));
    c.setOrganization(StringUtils.trim(request.getParameter("organization")));
    c.setRemarks(StringUtils.trim(request.getParameter("remarks")));
    c.setUrl(StringUtils.trim(request.getParameter("url")));
    c.setOperator(hikUserService.getCurrentUsername(request));
    return c;
  }

  /**
   * 关键词搜索CCC列表
   * 
   * @param field 搜索域
   * @param keyword 相应关键字
   * @param pageNum 页码 默认为第一页
   * @param pageSize 页大小 默认20条/页
   * @param sortBy 需要倒序排序的字段，默认为更新时间UpdateAt字段
   * @param direction <=0表示降序，>0为升序，默认为降序
   * @return 包含搜索结果的返回对象
   */
  @OperationLogIgnore
  @RequestMapping(value = "/search.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult searchCCC(Integer field, String keyword, Integer pageNum, Integer pageSize,
      String sortBy, Integer direction) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
    if (StringUtils.isBlank(sortBy)) {
      sortBy = SORTBY_AWARDDATE; // 默认按照颁发日期倒序
    }
    String fieldName;
    if (field == null) {
      fieldName = "";
    } else {
      switch (field) {
        case 1:
          fieldName = "docNo";
          break;
        case 2:
          fieldName = "model";
          break;
        case 3:
          fieldName = "productName";
          break;
        case 4:
          fieldName = "remarks";
          break;
        default:
          fieldName = "";
          break;
      }

    }
    if (StringUtils.isBlank(keyword)) {
      keyword = "";
    }

    String[] keywordList = StringUtils.split(keyword);
    ListResult res = new ListResult();
    try {
      Page<CccPage> p = cccPageService.searchCccByPage(fieldName, keywordList, pn, ps, sortBy, dir);
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
