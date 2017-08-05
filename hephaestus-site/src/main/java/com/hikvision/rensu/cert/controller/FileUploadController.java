package com.hikvision.rensu.cert.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hikvision.rensu.cert.constant.RetStatus;
import com.hikvision.rensu.cert.service.CccPageService;
import com.hikvision.rensu.cert.service.CopyrightService;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import com.hikvision.rensu.cert.support.ImportResult;

/**
 * 由于导入文件并没有区分类型，所以其实正确的做法应该在一个新的类中导入 Created by rensu on 2017/5/28.
 */
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

	static private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private TypeInspectionService typeInspectionService;
	
	@Autowired
	private CopyrightService copyrightService;
	
	@Autowired
	private CccPageService cccPageService;

	@RequestMapping(value = "/indexlist.do", method = RequestMethod.POST)
	@ResponseBody
	public ImportResult saveIndexList(@RequestBody MultipartFile file) {

		if (null == file || file.isEmpty()) {
			return new ImportResult(RetStatus.FILE_EMPTY.getCode(), RetStatus.FILE_EMPTY.getInfo(),0,0,0);
		} else {
			InputStream xlsxFile = null;
			int numOfInpections = 0;
			int numOfCopyRight = 0;
			int numOf3C = 0;
			/*
			 * Test excel Encryption & handle exceptions
			 */
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					if (StringUtils.contains(workbook.getSheetName(i), "公检")) {
						numOfInpections = typeInspectionService.importInspectionSheet(workbook.getSheetAt(i));
					} else if (StringUtils.contains(workbook.getSheetName(i), "双证")) {
						numOfCopyRight = copyrightService.importCopyRightSheet(workbook.getSheetAt(i));
					} else if (StringUtils.contains(workbook.getSheetName(i), "3C")) {
						numOf3C = cccPageService.importCCCSheet(workbook.getSheetAt(i));
					}
				}
				return new ImportResult(RetStatus.SUCCESS.getCode(), RetStatus.SUCCESS.getInfo(), numOfInpections, numOfCopyRight, numOf3C);
			} catch (IOException e) {
				logger.error("", e);
				return StringUtils.contains(e.getMessage(), "EncryptionInfo")
						? new ImportResult(RetStatus.FILE_ENCYPTED.getCode(), RetStatus.FILE_ENCYPTED.getInfo())
						: new ImportResult(RetStatus.FILE_PARSING_ERROR.getCode(), RetStatus.FILE_PARSING_ERROR.getInfo());
			} catch (EncryptedDocumentException | InvalidFormatException e) {
				logger.error("", e);
				return new ImportResult(RetStatus.FILE_INVALID.getCode(), RetStatus.FILE_INVALID.getInfo());
			} catch (Exception e) {
				logger.error("", e);
				if(StringUtils.contains(e.getCause().getCause().toString(), "duplicate")){
					String cause = e.getCause().getCause().toString().toString();
					return new ImportResult(RetStatus.DOCNO_DUPLICATED.getCode(),cause.substring(cause.indexOf("详细")+3));
				}
				return new ImportResult(RetStatus.FILE_PARSING_ERROR.getCode(), RetStatus.FILE_PARSING_ERROR.getInfo());
			} finally {
				IOUtils.closeQuietly(xlsxFile);
			}
		}
	}


}
