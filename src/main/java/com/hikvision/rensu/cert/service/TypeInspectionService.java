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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by rensu on 17/4/21.
 */
@Service
@Transactional
public class TypeInspectionService {

    private final TypeInspectionRepository typeInspectionRepository;

    private final InspectContentRepository inspectContentRepository;

    @Autowired
    public TypeInspectionService(TypeInspectionRepository typeInspectionRepository, InspectContentRepository inspectContentRepository) {
        this.typeInspectionRepository = typeInspectionRepository;
        this.inspectContentRepository = inspectContentRepository;
    }

    public Page<TypeInspection> getInspectionByPage(Integer pageNum, Integer pageSize) {
    	int pn = pageNum == null?1:pageNum.intValue();
    	int ps = pageSize ==null?20:pageSize.intValue(); // 默认20条/页
    	int start = (pn-1)*ps;
        Pageable page = new PageRequest(start, ps);
        return typeInspectionRepository.findAll(page);
    }

    public List<TypeInspection> getInspections() {
        return typeInspectionRepository.findAll();
    }

    public void save(TypeInspection typeInspection) {
        typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection get(Long id) {
        return typeInspectionRepository.findOne(id);
    }

    public void importTyepInspection(File xlsxFile) {

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
                    }
                }

                typeInspectionRepository.save(t);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importTypeContent(File xlsxFile) {

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
                    }
                }
                inspectContentRepository.save(content);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
