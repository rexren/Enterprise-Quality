package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.domain.TypeInspectionMapper;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import com.hikvision.rensu.cert.search.IndexService;
import io.searchbox.client.JestClient;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rensu on 17/4/21.
 */
@Service
@Transactional
public class TypeInspectionService {

    private final static Logger logger = LoggerFactory.getLogger(TypeInspectionService.class);

    private final static String SORT_TYPEINSPECTION_UPDATEAT = "UpdateAt";
    @Autowired
    IndexService indexService;
    @Autowired
    private TypeInspectionRepository typeInspectionRepository;
    @Autowired
    private InspectContentService inspectContentService;

    private TypeInspectionMapper mapper = new TypeInspectionMapper();

    public Page<TypeInspection> getInspectionByPage(Integer pageNum, Integer pageSize) {
        //TODO 参数验证放到controller中
        int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
        int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
        /* 按照更新时间降序排序（从先到后） */
        //TODO
        Pageable page = new PageRequest(pn, ps, new Sort(Direction.DESC, SORT_TYPEINSPECTION_UPDATEAT));
        return typeInspectionRepository.findAll(page);
    }

    public TypeInspection save(TypeInspection typeInspection) {
        return typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection getTypeInspectionById(Long id) {
        return typeInspectionRepository.findOne(id);
    }

    public void deleteTypeInspection(TypeInspection t) {
        typeInspectionRepository.delete(t);
    }

    /**
     * 导入列表数据 | 产品型号 | 软件名称 | 软件版本 | 测试／检验类别 | 受检单位 | 测试依据 | 颁发日期 | 文件编号 |
     * 证书系统链接 | 认证／测试机构 | 备注
     *
     * @param sheet: 【公检&国标】工作薄
     * @return res: 0更新成功， 1更新失败：列表为空，2更新失败:其他必填数据为空
     * @throws Exception
     * @author langyicong
     */
    public int importInspectionSheet(Sheet sheet) throws Exception {
        int res = 1;
        int rows = sheet.getLastRowNum() + 1;
        List<TypeInspection> inspections = new ArrayList<>();
        //TODO row = 2 找到表头并且 判断表头是否符合条件
        List<TypeInspection> dupItems = new ArrayList<TypeInspection>();
        for (int row = 2; row < rows; row++) {
            Row r = sheet.getRow(row);
            /* for rows those are not empty */
            if (r.getCell(0) != null && r.getCell(0).getCellTypeEnum() != CellType.BLANK
                    && r.getCell(1).getCellTypeEnum() != CellType.BLANK) {
                TypeInspection t = null;
                if (r.getCell(7) != null) {
                    if (r.getCell(7).getCellTypeEnum() != CellType.STRING) {
                        r.getCell(7).setCellType(CellType.STRING);
                    }
                    //如果docNo在数据库中存在
                    if (typeInspectionRepository.findByDocNo(r.getCell(7).getStringCellValue()).size() > 0) {
                        t = typeInspectionRepository.findByDocNo(r.getCell(7).getStringCellValue()).get(0);
                    } else {
                        t = new TypeInspection();
                    }
                    t.setDocNo(r.getCell(7).getStringCellValue());
                } else {
                    res = 2;
                    throw new Exception("Table cell(7) is empty, expected to be docNo");
                }


                t.setModel(r.getCell(0).getStringCellValue());
                t.setName(r.getCell(1).getStringCellValue());
                t.setVersion(r.getCell(2).getStringCellValue());
                t.setTestType(r.getCell(3).getStringCellValue());
                t.setCompany(r.getCell(4).getStringCellValue());
                t.setBasis(r.getCell(5).getStringCellValue());
                if (r.getCell(6) != null) {
                    if (r.getCell(6).getCellTypeEnum() != CellType.NUMERIC) {
                        r.getCell(6).setCellType(CellType.NUMERIC);
                    }
                    t.setAwardDate(r.getCell(6).getDateCellValue());
                } else {
                    res = 2;
                    throw new Exception("Table cell(6) is empty, expected to be awardDate");
                }

                t.setCertUrl(r.getCell(8).getStringCellValue());
                t.setOrganization(r.getCell(9).getStringCellValue());
                t.setRemarks(r.getCell(10).getStringCellValue());
                t.setCreateAt(new Date());
                t.setUpdateAt(new Date());
                t.setOperator("TESTER"); // TODO 获取当前用户

                inspections.add(t);
            }
        }
        if (inspections.size() > 0) {
            importInspectionList(inspections);
        }
        return res;
    }


    /**
     * 与数据库中的Inspections比较，如果docNo已经存在，则(删除后插入新数据)->更新数据，否则直接使用save方法
     *
     * @param inspections 待插入的List
     */
    public List<TypeInspection> importInspectionList(List<TypeInspection> inspections) {
        List<TypeInspection> dupItems = new ArrayList<TypeInspection>();
        //TODO:这里存在一个问题，在数据量大的情况下，大量的查表操作是在循环内，需要优化
        //TODO:导入一个表，一般可以先把现存的那部分先搜索出来，然后更新

        for (int i = 0; i < inspections.size(); i++) {
            dupItems = typeInspectionRepository.findByDocNo(inspections.get(i).getDocNo());
            if (dupItems.size() > 0) {
                /* delete and insert new one */
                typeInspectionRepository.delete(dupItems);
            }
            typeInspectionRepository.save(inspections.get(i));

            indexService.saveToIndex(mapper.map(inspections.get(i)));
        }
        return dupItems;
    }

    /**
     * 存储 & （如果文件名为空）删子表
     *
     * @return void
     */
    @Transactional

    public TypeInspection updateTypeInspection(String fileName, TypeInspection t) {
        Long inspectionId = t.getId();
    	/* String fname = request.getParameter("fileName");*/
        if (StringUtils.isBlank(fileName)) {
            t.setDocFilename("");
            inspectContentService.deleteByFK(inspectionId);
        }
        return typeInspectionRepository.save(t);

    }
}
