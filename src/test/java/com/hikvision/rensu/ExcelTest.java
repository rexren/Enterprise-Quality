package com.hikvision.rensu;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by rensu on 17/5/6.
 */
public class ExcelTest {

    @Test
    public void read() throws Exception {
        File xlsFile = new File("poi.xlsx");
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
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
                    Assert.assertEquals("data" + row + col, r.getCell(col).getStringCellValue());
                }
            }
        }
    }

    @Test
    public void write() throws Exception {
        // 创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        XSSFSheet sheet = workbook.createSheet("sheet1");

        for (int row = 0; row < 10; row++) {
            XSSFRow rows = sheet.createRow(row);
            for (int col = 0; col < 10; col++) {
                // 向工作表中添加数据
                rows.createCell(col).setCellValue("data" + row + col);
            }
        }

        File xlsxFile = new File("poi.xlsx");
        FileOutputStream xlsStream = new FileOutputStream(xlsxFile);
        workbook.write(xlsStream);
    }
}
