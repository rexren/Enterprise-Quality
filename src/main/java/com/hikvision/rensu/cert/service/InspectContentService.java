package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.repository.InspectContentRepository;

import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rensu on 17/4/28.
 */
@Service
public class InspectContentService {

    @Autowired
    private InspectContentRepository inspectContentRepository;

    public InspectContent get(Long id) {
        return inspectContentRepository.findOne(id);
    }
    
	/**
	 * 解析详细检测报告索引文件
	 * @param workbook excel工作薄
	 * @return void
	 * @author langyicong
	 */
    public void saveInspectContents(Workbook workbook){
 
        InspectContent content = new InspectContent();

        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() - 2;
        // 先获得一行，再获得列数
        Row tmp = sheet.getRow(0);
        // 第三行开始读取数据
        for (int row = 3; row < rows; row++) {
        	Row r = sheet.getRow(row);
        	/* rows those are not empty */
            if(r.getCell(0)!=null && r.getCell(0).getCellTypeEnum()!=CellType.BLANK 
            		&& r.getCell(1).getCellTypeEnum()!=CellType.BLANK){
                InspectContent c = new InspectContent();
                c.setCaseId((long) r.getCell(0).getNumericCellValue());
                c.setCaseName(r.getCell(1).getStringCellValue());
                c.setCaseDescription(r.getCell(2).getStringCellValue());
                c.setTestResult(r.getCell(3).getStringCellValue());
                inspectContentRepository.save(c);
            }
        }
        inspectContentRepository.save(content);
        
    }
    
}
