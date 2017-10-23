package com.hikvision.ga.hephaestus.cert.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;
import com.hikvision.ga.hephaestus.cert.domain.TypeInspection;
import com.hikvision.ga.hephaestus.cert.repository.InspectContentRepository;
import com.hikvision.ga.hephaestus.cert.repository.TypeInspectionJpaRepository;
import com.hikvision.ga.hephaestus.cert.repository.TypeInspectionRepository;
import com.hikvision.ga.hephaestus.cert.support.TypeSearchResult;


/**
 * Created by rensu on 17/4/21.
 */
@Service
@Transactional
public class TypeInspectionService {

  private final static Logger logger = LoggerFactory.getLogger(TypeInspectionService.class);

  @Autowired
  private TypeInspectionRepository typeInspectionRepository;
  
  @Autowired
  private TypeInspectionJpaRepository typeInspectionJpaRepository;

  @Autowired
  private InspectContentRepository inspectContentRepository;

  @Autowired
  private InspectContentService inspectContentService;

  /**
   * 获取型检TypeInspection页，包括按照颁发日期筛选
   * @param pageNum 页码
   * @param pageSize 页容量
   * @param sortBy 排序字段
   * @param dir 排序方向
   * @param start 颁发日期的起始时间
   * @param end 颁发日期的结束时间
   * @return TypeInspection页
   */
  public Page<TypeInspection> getInspectionByPage(int pageNum, int pageSize, String sortBy,
      int dir, Date start, Date end) {
    Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
    Pageable page = new PageRequest(pageNum, pageSize, new Sort(d, sortBy, "id"));
    try {
      // 默认计算结束时间的晚上最后一秒
      Date endOfTheDay = new Date(end.getTime() + 1000 * 60 * 60 * 24 - 1);
      Page<TypeInspection> p = typeInspectionJpaRepository.findTypeInspectionByTimeRange(start, endOfTheDay, page);
      return p;
    } catch (Exception e) {
      logger.error("", e);
      throw e;
    }
  }
  
  /**
   * 返回所有日期的结果
   * @param pageNum
   * @param pageSize
   * @param sortBy
   * @param dir
   * @return TypeInspection页结果
   */
  public Page<TypeInspection> getInspectionByPage(int pageNum, int pageSize, String sortBy,
      int dir) {
    Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
    Pageable page = new PageRequest(pageNum, pageSize, new Sort(d, sortBy, "id"));
    return typeInspectionRepository.findAll(page);
  }

  /**
   * 存储
   * @param typeInspection
   * @return
   */
  public TypeInspection save(TypeInspection typeInspection) {
    return typeInspectionRepository.save(typeInspection);
  }

  public TypeInspection getTypeInspectionById(Long id) {
    return typeInspectionRepository.findOne(id);
  }

  public List<TypeInspection> findByDocNo(String docNo) {
    return typeInspectionJpaRepository.findByDocNo(docNo);
  }

  public void deleteTypeInspection(TypeInspection t) {
    typeInspectionRepository.delete(t);
  }

  /**
   * 导入型检列表到数据库 产品型号 | 软件名称 | 软件版本 | 测试／检验类别 | 受检单位 | 测试依据 | 颁发日期 | 文件编号 |证书系统链接 | 认证／测试机构 | 备注
   *
   * @param sheet: 【公检&国标】工作薄
   * @return res: 更新/导入的条目数量
   * @throws Exception
   * @author langyicong
   */
  public int importInspectionSheet(Sheet sheet) throws Exception {
    int res = 0;
    int rows = sheet.getLastRowNum() + 1;
    List<TypeInspection> inspections = new ArrayList<>();
    /* 找到表头并且 判断表头是否符合条件 */
    int headRow = -1, startCol = -1, modelCol = -1, nameCol = -1, docNoCol = -1, awardDateCol = -1;
    for (int row = 0; row < rows; row++) {
      Row r = sheet.getRow(row);
      int cols = r.getPhysicalNumberOfCells();
      for (int col = 0; col < cols; col++) {
        if (r.getCell(col) != null) {
          if (r.getCell(col).getCellTypeEnum() != CellType.STRING) {
            r.getCell(col).setCellType(CellType.STRING);
          }
          String cellValue = r.getCell(col).getStringCellValue();
          if (StringUtils.containsAny(cellValue, "型号", "软件名称", "文件编号", "颁发日期")) {
            headRow = row;
            if (StringUtils.contains(cellValue, "型号")) {
              modelCol = col;
            }
            if (StringUtils.contains(cellValue, "软件名称")) {
              nameCol = col;
            }
            if (StringUtils.contains(cellValue, "文件编号")) {
              docNoCol = col;
            }
            if (StringUtils.contains(cellValue, "颁发日期")) {
              awardDateCol = col;
            }
          }
        }
      }
      if (row == headRow) {
        break;
      }
    }
    startCol = Math.min(modelCol, Math.min(nameCol, docNoCol));
    if (headRow < 0 || startCol < 0) {
      throw new Exception("缺少关键字");
    }

    for (int row = headRow + 1; row < rows; row++) {
      Row r = sheet.getRow(row);
      /* for rows those are not empty */
      if (r.getCell(startCol) != null && r.getCell(startCol).getCellTypeEnum() != CellType.BLANK
          && r.getCell(startCol + 1).getCellTypeEnum() != CellType.BLANK) {
        TypeInspection t = null;
        if (r.getCell(docNoCol) != null) {
          if (r.getCell(docNoCol).getCellTypeEnum() != CellType.STRING) {
            try {
              r.getCell(docNoCol).setCellType(CellType.STRING);
            } catch (Exception e) {
              throw e;
            }
          }
          List<TypeInspection> thisDocNoList = typeInspectionJpaRepository
              .findByDocNo(StringUtils.trim(r.getCell(docNoCol).getStringCellValue()));
          if (thisDocNoList.size() > 0) {
            /* if this docNo exists in db, find it and update other properties */
            t = thisDocNoList.get(0);
          } else {
            t = new TypeInspection();
            t.setCreateAt(new Date());
            t.setDocNo(StringUtils.trim(r.getCell(docNoCol).getStringCellValue()));
          }
        } else {
          throw new Exception("Missing docNo");
        }

        t.setModel(StringUtils.trim(r.getCell(modelCol).getStringCellValue()));
        t.setName(StringUtils.trim(r.getCell(nameCol).getStringCellValue()));
        t.setVersion(StringUtils.trim(r.getCell(2).getStringCellValue()));
        t.setTestType(StringUtils.trim(r.getCell(3).getStringCellValue()));
        t.setCompany(StringUtils.trim(r.getCell(4).getStringCellValue()));
        t.setBasis(r.getCell(5).getStringCellValue());

        if (r.getCell(awardDateCol) != null) {
          if (r.getCell(awardDateCol).getCellTypeEnum() != CellType.NUMERIC) {
            try {
              r.getCell(awardDateCol).setCellType(CellType.NUMERIC);
            } catch (Exception e) {
              throw e;
            }
          }
          t.setAwardDate(r.getCell(awardDateCol).getDateCellValue());
        }
        t.setCertUrl(r.getCell(8).getStringCellValue());
        t.setOrganization(r.getCell(9).getStringCellValue());
        t.setRemarks(r.getCell(10).getStringCellValue());
        t.setUpdateAt(new Date());
        // t.setOperator(systemUserService.getCurrentUsername());
        inspections.add(t);
      }
    }
    try {
      typeInspectionRepository.save(inspections);
      res = inspections.size();
    } catch (Exception e) {
      logger.error("", e);
      throw e;
    }
    return res;
  }

  /**
   * 新建存儲主表条目
   *
   * @param t 需要插入的TypeInspection实体
   * @param contentList 待插入数据库的检测报告内容表（子表）
   * @return TypeInspection实体
   */
  public TypeInspection saveTypeInspection(TypeInspection t, List<InspectContent> contentList) {
    if (contentList != null && contentList.size() > 0) {
      inspectContentRepository.save(contentList);
    }
    return typeInspectionRepository.save(t);
  }

  /**
   * 更新主表 &（如果文件名为空）删子表 &（如果有文件）存储子表
   *
   * @param t 需要更新的TypeInspection实体
   * @param contentList 待插入数据库的检测报告内容表（子表）
   * @return TypeInspection实体
   */
  public TypeInspection updateTypeInspection(TypeInspection t, List<InspectContent> contentList) {
    Long inspectionId = t.getId();
    /* 若文件名为空，删除子表 */
    if (StringUtils.isBlank(t.getDocFilename())) {
      inspectContentService.deleteByFK(inspectionId);
    }
    /* 如果contentList不为空，删除后插入 */
    else if (contentList != null && contentList.size() > 0) {
      List<InspectContent> listInDB = inspectContentService.getContentsByInspectionId(inspectionId);
      if (listInDB.size() > 0) {
        inspectContentRepository.deleteInBatch(listInDB);
      }
      inspectContentRepository.save(contentList);
    }
    return typeInspectionRepository.save(t);
  }

  /**
   * 模糊搜索关键字
   * @param fieldName 需要搜索的字段范围
   * @param keywordList 搜索字段相应关键词列表
   * @param searchRelation 关键词检索和内容检索之间的关系，“或”或者“与”，默认为“与”
   * @param contentKeywordList 内容搜索关键词列表
   * @param contentKeywordRelation 搜索内容关键字的关系，“或”或者“与”，默认为“与”
   * @return 搜索结果list
   *
   * @throws Exception
   */
  public List<TypeSearchResult> searchTypeInspectionByPage(String fieldName, String[] keywordList,
      String searchRelation, String[] contentKeywordList, String contentKeywordRelation) throws Exception {
    return typeInspectionRepository.joinSearchTypeInspection(fieldName, keywordList, searchRelation,
        contentKeywordList, contentKeywordRelation);
  }

  /**
   * 删除单条记录
   * @param id 
   * @throws IllegalArgumentException 
   *
   * @throws Exception
   */
  public void deleteTypeInspectionById(Long id) throws IllegalArgumentException {
    typeInspectionRepository.delete(id);
  }

}
