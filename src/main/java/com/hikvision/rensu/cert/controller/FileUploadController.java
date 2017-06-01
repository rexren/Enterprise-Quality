package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.constant.RetCode;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import com.hikvision.rensu.cert.support.BaseResult;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping(value = "/indexlist.do", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveIndexList(@RequestBody MultipartFile file) {

        if (null == file || file.isEmpty()) {
            return new BaseResult(RetCode.FILE_EMPTY_CODE, RetCode.FILE_EMPTY_INFO);
        } else {
            InputStream xlsxFile = null;

			/*
             * Test excel Encryption & handle exceptions
			 */
            try {
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                	//TODO String操作相关的改为StringUtils
                    if (StringUtils.contains(workbook.getSheetName(i), "公检")){
                        //importInspectionSheet(workbook.getSheetAt(i));
                    	typeInspectionService.importInspectionSheet(workbook.getSheetAt(i));
                    }
                    /**
                    else if (workbook.getSheetName(i).contains("双证")) {
                        importCopyRightSheet(workbook.getSheetAt(i));
                    } else if (workbook.getSheetName(i).contains("3C")) {
                        importCCCSheet(workbook.getSheetAt(i));
                    } else if (workbook.getSheetName(i).contains("更新说明")) {
                        // TODO: news can be done without sheet.
                    } else {
                    	return new BaseResult(RetCode.FILE_SHEET_MISSING_CODE, RetCode.FILE_SHEET_MISSING_CODE);
                    }*/
                }
                return new BaseResult(RetCode.SUCCESS_CODE);
            } catch (IOException e) {
            	BaseResult result = new BaseResult();
                if (e.getMessage().contains("EncryptionInfo")) {
                	result.setCode(RetCode.FILE_ENCYPTED_ERROR_CODE);
                	result.setMsg(RetCode.FILE_ENCYPTED_ERROR_INFO);
                } else {
                    result.setCode(RetCode.FILE_PARSING_ERROR_CODE);
                    result.setMsg(RetCode.FILE_PARSING_ERROR_INFO);
                }
                return result;
            } catch (EncryptedDocumentException | InvalidFormatException e) {
                logger.error("", e.getMessage());
                return new BaseResult(RetCode.FILE_INVALID_CODE, RetCode.FILE_INVALID_INFO);
            } catch (Exception e) {
                logger.error("", e.getMessage());
                return new BaseResult(RetCode.FILE_PARSING_ERROR_CODE, RetCode.FILE_PARSING_ERROR_INFO);
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

    private int importCCCSheet(Sheet sheet) throws NotImplementedException {
        throw new NotImplementedException("importCCCSheet not implement");
    }

    private int importCopyRightSheet(Sheet sheet) throws NotImplementedException {
        throw new NotImplementedException("importCopyRightSheet not implement");
    }
}
