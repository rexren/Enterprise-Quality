package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.constant.RetCode;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import com.hikvision.rensu.cert.support.PageResult;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 由于导入文件并没有区分类型，所以其实正确的做法应该在一个新的类中导入
 * Created by rensu on 2017/5/28.
 */
@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

    static private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private TypeInspectionService typeInspectionService;

    @RequestMapping(value = "/uploadIndexList.do", method = RequestMethod.POST)
    @ResponseBody
    public PageResult saveIndexList(@RequestBody MultipartFile file) {

        if (null == file || file.isEmpty()) {
            return new PageResult(RetCode.FILE_EMPTY_CODE, "empty file");
        } else {
            InputStream xlsxFile = null;

			/*
             * Test excel Encryption & handle exceptions
			 */
            try {
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    if (workbook.getSheetName(i).contains("公检")) {
                        Sheet sheet = workbook.getSheetAt(i);
                        importInspectionSheet(sheet);
                    } else if (workbook.getSheetName(i).contains("双证")) {
                        System.out.println(workbook.getSheetName(i));
                        // 导入双证解析
                    } else if (workbook.getSheetName(i).contains("3C")) {
                        System.out.println(workbook.getSheetName(i));
                        // 导入3C解析
                    } else if (workbook.getSheetName(i).contains("更新说明")) {
                        System.out.println(workbook.getSheetName(i));
                        // 导入更新说明
                    } else {
                        // TODO 错误：找不到sheet（每个controller只需一种测试）
                    }
                }
                return new PageResult(RetCode.SUCCESS_CODE);
            } catch (IOException e) {
                PageResult result = new PageResult();
                if (e.getMessage().contains("EncryptionInfo")) {
                    //TODO: exception occurs, must return errorcode
                    result.setCode(RetCode.FILE_ENCYPTED_ERROR_CODE);
                } else {
                    result.setCode(RetCode.FILE_PARSING_ERROR_CODE);
                }

                return result;
            } catch (EncryptedDocumentException | InvalidFormatException e) {
                logger.error("", e.getMessage());
                return new PageResult(RetCode.FILE_PARSING_ERROR_CODE, "invalid file");
            } catch (Exception e) {
                logger.error("", e.getMessage());
                return new PageResult(RetCode.FILE_PARSING_ERROR_CODE, "error file");
            } finally {
                /* 关闭文件流 */
                if (xlsxFile != null) {
                    try {
                        xlsxFile.close();
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
            }
        }
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
    private int importInspectionSheet(Sheet sheet) throws Exception {
        int res = 1;
        int rows = sheet.getLastRowNum() + 1;
        logger.debug("the total number of type inspection is {}.", rows);

        List<TypeInspection> inspections = new ArrayList<>();
        //TODO row = 2 找到表头并且 判断表头是否符合条件
        //// TODO: 2017/5/28 controller里面不要放业务逻辑
        for (int row = 2; row < rows; row++) {
            Row r = sheet.getRow(row);
			/* for rows those are not empty */
            if (r.getCell(0) != null && r.getCell(0).getCellTypeEnum() != CellType.BLANK
                    && r.getCell(1).getCellTypeEnum() != CellType.BLANK) {
                TypeInspection t = new TypeInspection();
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
                    return res;
                }
                if (r.getCell(7) != null) {
                    if (r.getCell(7).getCellTypeEnum() != CellType.STRING) {
                        r.getCell(7).setCellType(CellType.STRING);
                    }
                    t.setDocNo(r.getCell(7).getStringCellValue());
                } else {
                    res = 2;
                    return res;
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
            //typeInspectionService.importInspectionList(inspections);
        }
        return res;
    }
}
