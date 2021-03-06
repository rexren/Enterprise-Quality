package com.hikvision.ga.hephaestus.site.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;
import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;
import com.hikvision.ga.hephaestus.cert.service.InspectContentService;
import com.hikvision.ga.hephaestus.cert.service.TypeInspectionService;
import com.hikvision.ga.hephaestus.cert.support.TypeSearchResult;
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
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

  private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

  private final static String SORTBY_AWARDDATE = "awardDate";

  @Autowired
  private TypeInspectionService typeInspectionService;

  @Autowired
  private InspectContentService inspectContentService;

  @Autowired
  private HikUserService hikUserService;

  /**
   * 获取公检&国标文件列表页
   *
   * @param pageNum 页码 默认为第一页
   * @param pageSize 页大小 默认20条/页
   * @param sortBy 需要倒序排序的字段，默认为更新时间UpdateAt字段
   * @param direction <=0表示降序，>0为升序，默认为降序
   * @param startTime 颁发日期的起始时间，选填
   * @param endTime 颁发日期的结束时间
   * @return 包含型检数据对象的返回对象
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult getInspecionListByPage(Integer pageNum, Integer pageSize, String sortBy,
      Integer direction, Long startTime, Long endTime) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
    if (StringUtils.isBlank(sortBy)) {
      sortBy = SORTBY_AWARDDATE; // 默认按照颁发日期和ID排序倒序
    }
    ListResult res = new ListResult();
    /* 如果选择了起止时间，则进行时间判断和转换 */
    if (startTime != null && endTime != null) {
      try {
        Date start = new Date(startTime);
        Date end = new Date(endTime);
        if (start.after(end)) {
          throw new Exception("时间先后有误");
        }
        Page<TypeInspection> p =
            typeInspectionService.getInspectionByPage(pn, ps, sortBy, dir, start, end);
        if (null != p) {
          res.setListContent(new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(),
              p.getContent()));
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
    // 如果起始和结束时间不符合要求
    try {
      Page<TypeInspection> p = typeInspectionService.getInspectionByPage(pn, ps, sortBy, dir);
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
  public BaseResult deleteInspecionById(Long id) {
    // 写日志:动作和模块
    OperationLogBuilder.build().act(OperationAct.DELETE).businessType(BusinessType.INSPECTIONS);
    BaseResult res = new BaseResult();
    if (null == id) {
      res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
      res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.PARAM_ILLEGAL.getCode())
          .log();
      return res;
    }
    try {
      // 操作日志：写id
      OperationLogBuilder.build().operateObjectId(Long.toString(id));
      TypeInspection t = typeInspectionService.getTypeInspectionById(id);
      // 操作日志：记录信息
      OperationLogBuilder.build().operateObjectId(t.getId().toString())
          .operateObjectKeys("model,name,docNo").operateObjectValues(t.getModel().toString() + ","
              + t.getName().toString() + "," + t.getDocNo().toString());
      typeInspectionService.deleteTypeInspectionById(id);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作结果：成功
      OperationLogBuilder.build().operateResult(1);
    } catch (IllegalArgumentException e) {
      logger.error("", e);
      res.setCode(RetStatus.USER_NOT_FOUND.getCode());
      res.setMsg(RetStatus.USER_NOT_FOUND.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.USER_NOT_FOUND.getCode());
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode());
    }
    // 操作日志：保存记录
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 获取单条型检数据（不包括列表）
   *
   * @param id 型检id
   * @return 包含型检数据对象的返回对象
   */
  @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
  @ResponseBody
  public AjaxResult<TypeInspection> getInspecionById(Long id) {
    AjaxResult<TypeInspection> res = new AjaxResult<TypeInspection>();
    if (null == id) {
      res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
      res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
    } else {
      try {
        TypeInspection t = typeInspectionService.getTypeInspectionById(id);
        if (t != null) {
          res.setData(t);
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
   * 获取单条型检数据的报告内容列表
   *
   * @param request http request
   * @return 包含报告内容列表的返回对象
   */
  @OperationLogIgnore
  @RequestMapping(value = "/contents.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult getContentsByInspectionId(HttpServletRequest request) {
    ListResult res = new ListResult();
    if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
      res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
      res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      return res;
    }
    Long id = NumberUtils.toLong(request.getParameter("id"));

    try {
      List<InspectContent> contents = inspectContentService.getContentsByInspectionId(id);
      if (contents != null && !contents.isEmpty()) {
        res.setListContent(new ListContent(contents.size(), (long) contents.size(), 1, contents));
        res.setCode(RetStatus.SUCCESS.getCode());
        res.setMsg(RetStatus.SUCCESS.getInfo());
      } else {
        res.setCode(RetStatus.SUCCESS.getCode());
        res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
      }
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
    }

    return res;
  }

  /**
   * 新建保存单条数据（包括报告详情文件）
   *
   * @param file 表单文件
   * @param request 表单内容
   * @return res 返回状态
   * @author langyicong
   */
  @HikRoleAccess(roles = HikRole.ADMIN)
  @RequestMapping(value = "/save.do", method = RequestMethod.POST)
  @ResponseBody
  public BaseResult saveInspectionForm(@RequestBody MultipartFile file,
      HttpServletRequest request) {
    // 操作日志：获取操作模块和动作
    OperationLogBuilder.build().act(OperationAct.ADD).businessType(BusinessType.INSPECTIONS);

    BaseResult res = new BaseResult();
    /* 表单验证:关键信息不为空 */
    if (StringUtils.isBlank(request.getParameter("model"))
        || StringUtils.isBlank(request.getParameter("name"))
        || StringUtils.isBlank(request.getParameter("awardDate"))
        || StringUtils.isBlank(request.getParameter("docNo"))) {
      res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
      res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
      // 操作日志：记录错误信息
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FORM_DATA_MISSING.getCode())
          .log();
      return res;
    }
    /* 表单验证:docNo不能重复 */
    String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
    if (typeInspectionService.findByDocNo(docNoStripped).size() > 0) {
      res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
      res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
      // 操作日志：记录错误信息
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.DOCNO_DUPLICATED.getCode())
          .log();
      return res;
    }
    /* parse excel file and save item */
    TypeInspection t = new TypeInspection();
    InputStream xlsxFile = null;
    List<InspectContent> contentlist = null;
    try {
      /* 页面参数组装成TypeInspection实体 */
      t = setTypeInspectionProperties(request, t);
      if (file != null && !file.isEmpty()) {
        /* Test excel Encryption & handle exceptions */
        xlsxFile = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(xlsxFile);
        Sheet contentSheet = workbook.getSheetAt(0);
        contentlist = inspectContentService.importContentSheet(contentSheet, t.getId());
        // 操作日志：记录上传的excel文件名
        OperationLogBuilder.build().content(t.getDocFilename());
      }
      t = typeInspectionService.saveTypeInspection(t, contentlist);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作日志
      OperationLogBuilder.build().operateObjectId(t.getId().toString())
          .operateObjectKeys("model,name,docNo").operateObjectValues(t.getModel().toString() + ","
              + t.getName().toString() + "," + t.getDocNo().toString())
          .operateResult(1);
    } catch (IOException e) {
      logger.error("", e);
      if (StringUtils.contains(e.getMessage(), "EncryptionInfo")) {
        res.setCode(RetStatus.FILE_ENCYPTED.getCode());
        res.setMsg(RetStatus.FILE_ENCYPTED.getInfo());
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_ENCYPTED.getCode());
      } else {
        res.setCode(RetStatus.FILE_PARSING_ERROR.getCode());
        res.setMsg(RetStatus.FILE_PARSING_ERROR.getInfo());
        OperationLogBuilder.build().operateResult(0)
            .errorCode(RetStatus.FILE_PARSING_ERROR.getCode());
      }
      // 写失败的日志
      OperationLogBuilder.build().log();
      return res;
    } catch (EncryptedDocumentException | InvalidFormatException e) {
      logger.error("", e);
      res.setCode(RetStatus.FILE_INVALID.getCode());
      res.setMsg(RetStatus.FILE_INVALID.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_INVALID.getCode())
          .log();
      return res;
    } catch (IllegalArgumentException e) {
      res.setCode(RetStatus.FILE_SHEET_MISSING.getCode());
      res.setMsg(RetStatus.FILE_SHEET_MISSING.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_SHEET_MISSING.getCode())
          .log();
      return res;
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode())
          .log();
      return res;
    } finally {
      IOUtils.closeQuietly(xlsxFile);
    }
    // 成功的情况下写日志
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 更新单条数据（包括报告详情文件）
   * 
   * @param file 待上传的列表文件（如果有的话）
   * @param request 其他表单参数
   * @return 成功与否的结果
   */
  @HikRoleAccess(roles = HikRole.ADMIN)
  @RequestMapping(value = "/update.do", method = RequestMethod.POST)
  @ResponseBody
  public BaseResult updateInspection(@RequestBody MultipartFile file, HttpServletRequest request) {
    // 操作日志：记录操作模块和动作
    OperationLogBuilder.build().act(OperationAct.UPDATE).businessType(BusinessType.INSPECTIONS);

    BaseResult res = new BaseResult();
    /* if null id or null entity */
    if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
      res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
      res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
      // 操作日志：记录失败动作
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.PARAM_ILLEGAL.getCode())
          .log();
      return res;
    }
    // 操作日志：记录id
    OperationLogBuilder.build().operateObjectId(request.getParameter("id"));

    /* 查询对应的实体，并且将页面数据更新实体内容 */
    Long inspectionId = NumberUtils.toLong(request.getParameter("id"));
    TypeInspection t = null;
    InputStream xlsxFile = null;
    try {
      t = typeInspectionService.getTypeInspectionById(inspectionId);
      if (null == t) {
        res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
        res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.ITEM_NOT_FOUND.getCode())
            .log();
        return res;
      }
      // docNo不能和除这条id对应的docNo之外的条目重复
      String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
      if (typeInspectionService.findByDocNo(docNoStripped).size() > 0
          && !StringUtils.equals(t.getDocNo(), docNoStripped)) {
        res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
        res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.DOCNO_DUPLICATED.getCode())
            .log();
        return res;
      }
      // 操作日志：记录条目信息
      OperationLogBuilder.build().operateObjectKeys("model,name,docNo").operateObjectValues(
          t.getModel().toString() + "," + t.getName().toString() + "," + t.getDocNo().toString());
      t = setTypeInspectionProperties(request, t); // 更新实体，包括文件名docFileName
      List<InspectContent> contentlist = null;
      if (file != null && !file.isEmpty()) {
        /* Test excel Encryption & handle exceptions */
        xlsxFile = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(xlsxFile);
        Sheet contentSheet = workbook.getSheetAt(0);
        contentlist = inspectContentService.importContentSheet(contentSheet, inspectionId);
        // 操作日志：写入文件名
        OperationLogBuilder.build().content(t.getDocFilename());
      }
      t = typeInspectionService.updateTypeInspection(t, contentlist);
      res.setCode(RetStatus.SUCCESS.getCode());
      res.setMsg(RetStatus.SUCCESS.getInfo());
      // 操作结果：成功
      OperationLogBuilder.build().operateObjectKeys("model,name,docNo").operateObjectValues(
          t.getModel().toString() + "," + t.getName().toString() + "," + t.getDocNo().toString())
          .operateResult(1);
    } catch (IOException e) {
      logger.error("", e);
      if (StringUtils.contains(e.getMessage(), "EncryptionInfo")) {
        res.setCode(RetStatus.FILE_ENCYPTED.getCode());
        res.setMsg(RetStatus.FILE_ENCYPTED.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_ENCYPTED.getCode());
      } else {
        res.setCode(RetStatus.FILE_PARSING_ERROR.getCode());
        res.setMsg(RetStatus.FILE_PARSING_ERROR.getInfo());
        // 操作日志：记录失败动作
        OperationLogBuilder.build().operateResult(0)
            .errorCode(RetStatus.FILE_PARSING_ERROR.getCode());
      }
      OperationLogBuilder.build().log();
      return res;
    } catch (EncryptedDocumentException | InvalidFormatException e) {
      logger.error("", e);
      res.setCode(RetStatus.FILE_INVALID.getCode());
      res.setMsg(RetStatus.FILE_INVALID.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_INVALID.getCode())
          .log();
      return res;
    } catch (IllegalArgumentException e) {
      res.setCode(RetStatus.FILE_SHEET_MISSING.getCode());
      res.setMsg(RetStatus.FILE_SHEET_MISSING.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_SHEET_MISSING.getCode())
          .log();
      return res;
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.SYSTEM_ERROR.getCode())
          .log();
      return res;
    } finally {
      IOUtils.closeQuietly(xlsxFile);
    }
    // 成功的情况下写日志
    OperationLogBuilder.build().log();
    return res;
  }

  /**
   * 将HttpServletRequest请求体（页面参数）组装成TypeInspection实体
   *
   * @param request 页面表单数据
   * @param t 初始的TypeInspection实体，可为空（新建）或者含id（更新）
   * @return t 转换后的TypeInspection实体
   * @throws Exception
   */
  private TypeInspection setTypeInspectionProperties(HttpServletRequest request, TypeInspection t)
      throws Exception {
    if (null == t) {
      t = new TypeInspection();
    }
    /* 传入的是新建的实体 */
    if (null == t.getId() || t.getId() <= 0) {
      t.setCreateAt(new Date());
    }
    /* setAwardDate */
    long awardDateLong = NumberUtils.toLong(request.getParameter("awardDate"));
    Date awardDate = new Date(awardDateLong);
    t.setAwardDate(awardDate);
    /* setDocFilename */
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    try {
      MultipartFile file = multipartRequest.getFile("file");
      if (null != file) {
        t.setDocFilename(file.getOriginalFilename());
      }
    } catch (Exception e) {
      throw e;
    } finally {
      /* set other properties */
      t.setModel(StringUtils.trim(request.getParameter("model")));
      t.setName(StringUtils.trim(request.getParameter("name")));
      t.setVersion(StringUtils.trim(request.getParameter("version")));
      t.setTestType(request.getParameter("testType"));
      t.setCompany(request.getParameter("company"));
      t.setBasis(request.getParameter("basis"));
      t.setDocNo(StringUtils.trim(request.getParameter("docNo")));
      t.setCertUrl(StringUtils.trim(request.getParameter("certUrl")));
      t.setOrganization(request.getParameter("organization"));
      t.setRemarks(request.getParameter("remarks"));
      t.setOperator(hikUserService.getCurrentUsername(request));
      t.setUpdateAt(new Date());
    }
    return t;
  }

  /**
   * 关键词搜索TypeInspection列表
   * 
   * @param field 需要搜索的字段编码
   * @param keyword 关键词
   * @param searchRelation 关键词检索和内容检索之间的关系，“或”或者“与”，默认为“与”
   * @param contentKeyword 内容搜索关键词
   * @param contentKeywordRelation 搜索内容关键字的关系，“或”或者“与”，默认为“与”
   * @param pageNum 页码 默认为第一页
   * @param pageSize 页大小 默认20条/页
   * @param sortBy 需要倒序排序的字段，默认为更新时间UpdateAt字段
   * @param direction <=0表示降序，>0为升序，默认为降序
   * @return 包含搜索结果的的返回对象
   */
  @RequestMapping(value = "/search.do", method = RequestMethod.GET)
  @ResponseBody
  public ListResult searchTypeInspection(Integer field, String keyword, String searchRelation,
      String contentKeyword, String contentKeywordRelation, Integer pageNum, Integer pageSize,
      String sortBy, Integer direction) {
    int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
    int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
    int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
    if (StringUtils.isBlank(sortBy)) {
      sortBy = SORTBY_AWARDDATE; // 默认按照颁发日期和ID排序倒序
    }
    ListResult res = new ListResult();

    String fieldName;
    if (field == null) {
      fieldName = "";
    } else {
      switch (field) {
        case 1:
          fieldName = "model";
          break;
        case 2:
          fieldName = "name";
          break;
        case 3:
          fieldName = "testType";
          break;
        case 4:
          fieldName = "basis";
          break;
        case 5:
          fieldName = "docNo";
          break;
        case 6:
          fieldName = "organization";
          break;
        case 7:
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
    if (StringUtils.isBlank(contentKeyword)) {
      contentKeyword = "";
    }
    if (StringUtils.equalsIgnoreCase(searchRelation, "OR")) {
      searchRelation = "OR";
    } else {
      searchRelation = "AND";
    }
    if (StringUtils.equalsIgnoreCase(contentKeywordRelation, "OR")) {
      contentKeywordRelation = "OR";
    } else {
      contentKeywordRelation = "AND";
    }

    String[] keywordList = StringUtils.split(keyword);

    String[] contentKeywordList = StringUtils.split(contentKeyword);

    try {
      List<TypeSearchResult> p = typeInspectionService.searchTypeInspectionByPage(fieldName,
          keywordList, searchRelation, contentKeywordList, contentKeywordRelation);

      Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
      Pageable page = new PageRequest(pn, ps, new Sort(d, sortBy, "id"));

      if (null != p) {
        PageImpl<TypeSearchResult> resPage = new PageImpl<TypeSearchResult>(p, page, p.size());
        res.setListContent(new ListContent(resPage.getSize(), resPage.getTotalElements(),
            resPage.getTotalPages(), resPage.getContent()));
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
