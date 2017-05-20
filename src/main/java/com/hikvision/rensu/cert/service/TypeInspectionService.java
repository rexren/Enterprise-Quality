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
     */
    public void importTyepInspection(InputStream xlsxFile) {

        try {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsxFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表
            for (int i = 0; i < sheetCount; i++) {

                TypeInspection t = new TypeInspection();

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
                        //TODO 对每行数据r处理，存入TypeInspection t
                        //TODO: each loop we create a new object t, bad taste.
                    }
                }

                typeInspectionRepository.save(t);
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
