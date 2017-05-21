package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.InspectContentRepository;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private InspectContentRepository inspectContentRepository;

    public Page<TypeInspection> getInspectionByPage(Integer pageNum, Integer pageSize) {
        int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
        int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
        Pageable page = new PageRequest(pn, ps);
        return typeInspectionRepository.findAll(page);
    }

    public void save(TypeInspection typeInspection) {
        typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection getTypeInspectionById(Long id) {
        return typeInspectionRepository.findOne(id);
    }

    /**
     * 导入列表数据（推荐手工导入数据库）
     * | 产品型号 | 软件名称 | 软件版本 |  测试／检验类别 | 受检单位 | 测试依据 | 颁发日期 | 文件编号 | 证书系统链接 | 认证／测试机构 | 备注
     */
    public void importTyepInspection(InputStream xlsxFile) {

        try {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsxFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();

            if (sheetCount > 5) {
                //the <公安事业部相关产品资质汇总表sheet页超过5>
                Sheet sheet = workbook.getSheetAt(1);

                int rows = sheet.getLastRowNum() - 2;
                logger.debug("the total number of type inspection is {}.", rows);

                List<TypeInspection> inspections = new ArrayList<>();
                for (int row = 0; row < rows; row++) {
                    Row r = sheet.getRow(row);
                    TypeInspection t = new TypeInspection();

                    t.setModel(r.getCell(0).getStringCellValue());
                    t.setName(r.getCell(1).getStringCellValue());
                    t.setVersion(r.getCell(2).getStringCellValue());
                    t.setTestType(r.getCell(3).getStringCellValue());
                    t.setCompany(r.getCell(4).getStringCellValue());
                    t.setBasis(r.getCell(5).getStringCellValue());
                    t.setAwardDate(r.getCell(6).getDateCellValue());
                    t.setDocNo(r.getCell(7).getStringCellValue());
                    t.setCertUrl(r.getCell(8).getStringCellValue());
                    t.setOrganization(r.getCell(9).getStringCellValue());
                    t.setRemarks(r.getCell(10).getStringCellValue());

                    inspections.add(t);
                }
                typeInspectionRepository.save(inspections);
            }

        } catch (InvalidFormatException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 保存检测报告数据表
     */
    public void importTypeContent(InputStream xlsxFile) {

        try {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsxFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表
            for (int i = 0; i < sheetCount; i++) {

                InspectContent content = new InspectContent();


                Sheet sheet = workbook.getSheetAt(i);
                // 获得行数
                int rows = sheet.getLastRowNum() + 1;
                // 获得列数，先获得一行，在得到改行列数
                Row tmp = sheet.getRow(0);
                if (tmp == null) {
                    continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                // 读取数据
                for (int row = 0; row < rows; row++) {
                    Row r = sheet.getRow(row);
                    for (int col = 0; col < cols; col++) {
                        //data put into content
                    }
                }
                inspectContentRepository.save(content);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //TODO 加密文件报错捕获
            e.printStackTrace();
        }
    }
}
