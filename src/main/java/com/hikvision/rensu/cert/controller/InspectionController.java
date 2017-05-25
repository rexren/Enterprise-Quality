package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.constant.RetCode;
import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.InspectContentService;
import com.hikvision.rensu.cert.service.TypeInspectionService;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

	@Autowired
	private TypeInspectionService typeInspectionService;

	@Autowired
	private InspectContentService inspectContentService;

	/**
	 * 获取公检&国标文件列表页
	 * TODO 此处不需要获取子表内容
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public Page<TypeInspection> getInspecionListByPage(Integer pageNum, Integer pageSize) {
		return typeInspectionService.getInspectionByPage(pageNum, pageSize);
	}

	/**
	 * 获取单条数据（不包括列表）
	 */
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public TypeInspection getInspecion(Long id) {
		return typeInspectionService.getTypeInspectionById(id);
	}

	/**
	 * 导入列表文件
	 */
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveInspectionList(@RequestBody MultipartFile file) throws Exception {
		Map<String, Object> res = new LinkedHashMap<String, Object>();

		if (null == file || file.isEmpty()) {
			res.put("code", RetCode.FILE_EMPTY_CODE);
			res.put("status", "empty file");
		} else {
			String fileName = file.getOriginalFilename();
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			logger.debug("the upload file name is {}.", fileName + suffixName);
			InputStream xlsxFile = null;

			/*
			 * Test excel Encryption & handle exceptions
			 */
			try {
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				int sheetCount = workbook.getNumberOfSheets();
				for (int i = 0; i < sheetCount; i++) {
					if (Pattern.compile("公检").matcher(workbook.getSheetName(i)).find()) {
						System.out.println(workbook.getSheetName(i));
						Sheet sheet = workbook.getSheetAt(i);
						importInspectionSheet(sheet);
						// typeInspectionService.importInspectionList(sheet);
					} else if (Pattern.compile("双证").matcher(workbook.getSheetName(i)).find()) {
						System.out.println(workbook.getSheetName(i));
						// 导入双证解析
					} else if (Pattern.compile("3C").matcher(workbook.getSheetName(i)).find()) {
						System.out.println(workbook.getSheetName(i));
						// 导入3C解析
					} else if (Pattern.compile("更新说明").matcher(workbook.getSheetName(i)).find()) {
						System.out.println(workbook.getSheetName(i));
						// 导入更新说明
					} else {
						// TODO 错误：找不到sheet（每个controller只需一种测试）
					}
				}
				res.put("code", RetCode.SUCCESS_CODE);
			} catch (IOException e) {
				String errMessage = e.getMessage();
				logger.error("", e);
				res.put("error", e.getMessage());
				String regExp = "EncryptionInfo";
				if (Pattern.compile(regExp).matcher(errMessage).find()) {
					res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
				} else {
					res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				}
				return res;
			} catch (EncryptedDocumentException | InvalidFormatException e) {
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
				return res;
			} catch (Exception e) {
				logger.error("", e);
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
				return res;
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
		return res;
	}

	/**
	 * 导入列表数据 | 产品型号 | 软件名称 | 软件版本 | 测试／检验类别 | 受检单位 | 测试依据 | 颁发日期 | 文件编号 |
	 * 证书系统链接 | 认证／测试机构 | 备注
	 * 
	 * @param sheet:
	 *            【公检&国标】工作薄
	 * @return res: 0更新成功， 1更新失败：列表为空，2更新失败:其他必填数据为空
	 * @author langyicong
	 * @throws Exception
	 */
	private int importInspectionSheet(Sheet sheet) throws Exception {
		int res = 1;
		int rows = sheet.getLastRowNum() - 2;
		logger.debug("the total number of type inspection is {}.", rows);

		List<TypeInspection> inspections = new ArrayList<>();
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
				}
				if (r.getCell(7) != null) {
					if (r.getCell(7).getCellTypeEnum() != CellType.STRING) {
						r.getCell(7).setCellType(CellType.STRING);
					}
					t.setDocNo(r.getCell(7).getStringCellValue());
				} else {
					res = 2;
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
			typeInspectionService.importInspectionList(inspections);
		}
		return res;
	}

	/**
	 * 新建保存单条数据（包括报告详情文件）
	 * 
	 * @param file
	 *            表单文件
	 * @param request
	 *            表单内容
	 * @return res Map->Json 返回状态
	 * @author langyicong
	 */
	@RequestMapping(value = "/save.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveInspectionForm(@RequestBody MultipartFile file, HttpServletRequest request) {
		Map<String, Object> res = new LinkedHashMap<String, Object>();
		TypeInspection t = new TypeInspection();
		/* save request form data to typeInspection table */
		try {
			t = typeInspectionService.saveSingleTypeInspection(request);
		} catch (Exception e) {
			logger.error("", e);
			res.put("error", e.getMessage());
			res.put("code", RetCode.UPDATE_DB_ERROR_CODE);
			return res;
		}

		/* parse and save excel file */
		if (null == file || file.isEmpty()) {
			res.put("code", RetCode.FILE_EMPTY_CODE);
			res.put("status", "empty file");
		} else {
			String fileName = file.getOriginalFilename();
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			logger.debug("the upload file name is {}.", fileName + suffixName);
			InputStream xlsxFile = null;

			/* Test excel Encryption & handle exceptions */
			try {
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				Sheet contentSheet = workbook.getSheetAt(0);
				int importRes = importContentSheet(contentSheet, t);
				switch (importRes) {
				case 0:
					res.put("code", RetCode.SUCCESS_CODE);
					break;
				case 1:
					res.put("code", RetCode.FILE_EMPTY_CODE);
					res.put("status", "empty file");
					break;
				case 2:
					res.put("code", RetCode.FILE_KEYWORD_ERROR_CODE);
					break;
				}
			} catch (IOException e) {
				String errMessage = e.getMessage();
				logger.error("", e);
				res.put("error", e.getMessage());
				String regExp = "EncryptionInfo";
				if (Pattern.compile(regExp).matcher(errMessage).find()) {
					res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
				} else {
					res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				}
				return res;
			} catch (EncryptedDocumentException | InvalidFormatException e) {
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
				return res;
			} catch (Exception e) {
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
				return res;
			} finally {
				if (xlsxFile != null) {
					try {
						xlsxFile.close();
					} catch (IOException e) {
						res.put("error", e.getMessage());
						res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
						logger.error("", e);
						return res;
					}
				}
			}
		}
		return res;
	}

	/**
	 * 更新单条数据（包括报告详情文件） TODO delete and then insert
	 */
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateInspection(@RequestBody MultipartFile file, HttpServletRequest request) {
		// TODO request.getParameter("id") NOT NULL
		Map<String, Object> res = new LinkedHashMap<String, Object>();

		try {
			typeInspectionService.updateTypeInspection(request);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/* parse and save excel file */
		if (null == file || file.isEmpty()) {
			res.put("code", RetCode.FILE_EMPTY_CODE);
			res.put("status", "empty file");
		} else {
			// TODO 比较和更新excel工作簿文件
			String fileName = file.getOriginalFilename();
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			logger.debug("the upload file name is {}.", fileName + suffixName);

			/* 读取文件流 */
			InputStream xlsxFile = null;

			/*
			 * Test excel Encryption & handle exceptions
			 */
			try {
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				// TODO 解析检测报告的excel文档

				res.put("code", RetCode.SUCCESS_CODE);
			} catch (IOException e) {
				String errMessage = e.getMessage();
				logger.error("", e);
				res.put("error", e.getMessage());
				String regExp = "EncryptionInfo";
				if (Pattern.compile(regExp).matcher(errMessage).find()) {
					res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
				} else {
					res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				}
			} catch (EncryptedDocumentException | InvalidFormatException e) {
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
			} catch (Exception e) {
				res.put("error", e.getMessage());
				res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
				logger.error("", e);
			} finally {
				if (xlsxFile != null) {
					try {
						xlsxFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return res;
	}

	/**
	 * 导入检测报告
	 * 
	 * @param contentSheet:
	 *            检测项索引表工作表
	 * @return res: 0更新成功， 1更新失败：列表为空，2更新失败:缺少关键字
	 * @author langyicong
	 * @throws Exception
	 */
	// TODO 解析详细检测报告索引文件并且【保存到子表中】！
	private int importContentSheet(Sheet contentSheet, TypeInspection t) throws Exception {
		int res = 1;
		int rows = contentSheet.getLastRowNum() - 2;
		logger.debug("the total number of inspection content list is {}.", rows);
		List<InspectContent> contentList = new ArrayList<>();

		/* 寻找表头所在行&每个字段所在列 */
		int headRow = -1, caseIdCol = -1, caseNameCol = -1, caseDescrCol = -1, testResultCol = -1;
		for (int row = 0; row < rows; row++) {
			Row r = contentSheet.getRow(row);
			int cols = r.getPhysicalNumberOfCells();
			for (int col = 0; col < cols; col++) {
				if (r.getCell(col) != null) {
					if (r.getCell(col).getCellTypeEnum() != CellType.STRING) {
						r.getCell(col).setCellType(CellType.STRING);
					}
				}
				String cellValue = r.getCell(col).getStringCellValue();
				if (Pattern.compile("序.*号").matcher(cellValue).find()) {
					headRow = row;
					System.out.println(cellValue);
					caseIdCol = col;
				}
				if (Pattern.compile("检测项目|检验项目|测试项目|用例名称|功能列表").matcher(cellValue).find()) {
					System.out.println(cellValue);
					caseNameCol = col;
				}
				if (Pattern.compile("(.*要求)|用例说明|功能描述").matcher(cellValue).find()) {
					System.out.println(cellValue);
					caseDescrCol = col;
				}
				if (Pattern.compile("测试结果").matcher(cellValue).find()) {
					System.out.println(cellValue);
					testResultCol = col;
				}
			}
			if (row == headRow) {
				break;
			}
		}

		if (caseIdCol < 0 || caseNameCol < 0 || caseDescrCol < 0) {
			res = 2;
			return res;
		}

		String cachedCaseId = "";
		String cachedCaseName = "";
		for (int row = headRow; row < rows; row++) {
			Row r = contentSheet.getRow(row);
			/* for rows those are not empty */
			if (r.getCell(caseDescrCol) != null && r.getCell(caseDescrCol).getCellTypeEnum() != CellType.BLANK) {
				InspectContent c = new InspectContent();
				if (r.getCell(caseIdCol) != null && r.getCell(caseIdCol).getCellTypeEnum() != CellType.BLANK) {
					if (r.getCell(caseIdCol).getCellTypeEnum() != CellType.STRING) {
						r.getCell(caseIdCol).setCellType(CellType.STRING);
					}
					cachedCaseId = r.getCell(caseIdCol).getStringCellValue();
				} 
				c.setCaseId(cachedCaseId);
				
				if (r.getCell(caseNameCol) != null && r.getCell(caseNameCol).getCellTypeEnum() != CellType.BLANK) {
					cachedCaseName = r.getCell(caseNameCol).getStringCellValue();
				} 
				c.setCaseName(cachedCaseName);
				
				if (caseDescrCol >= 0) {
					c.setCaseDescription(r.getCell(caseDescrCol).getStringCellValue());
				}
				if (testResultCol >= 0) {
					c.setTestResult(r.getCell(testResultCol).getStringCellValue());
				}
				contentList.add(c);
			}
		}
		inspectContentService.importContentList(contentList, t);
		res = 0;
		return res;
	}
}
