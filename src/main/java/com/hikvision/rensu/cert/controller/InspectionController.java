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
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
//TODO PageResult格式返回
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
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public Page<TypeInspection> getInspecionListByPage(Integer pageNum, Integer pageSize) {
		return typeInspectionService.getInspectionByPage(pageNum, pageSize);
	}

	/**
	 * 获取单条型检数据（不包括列表）
	 * @param id 型检id
	 * @return t 型检数据
	 */
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public TypeInspection getInspecion(Long id) {
		TypeInspection t = typeInspectionService.getTypeInspectionById(id);
		return t;
	}
	
	/**
	 * 获取单条型检数据的报告内容列表
	 * @param id 型检id
	 * @return contents 报告内容列表
	 */
	@RequestMapping(value = "/contents.do", method = RequestMethod.GET)
	@ResponseBody
	public List<InspectContent> getContents(Long id) {
		List<InspectContent> contents = inspectContentService.getContentsAll(id);
		return contents;
	}
	
	/**
	 * 列表页(/inspections) 导入列表文件
	 */
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveInspectionList(@RequestBody MultipartFile file) {
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
		//TODO DEBUG 事务：如果文件被加密，也是不能插入数据的才对
		//TODO 业务逻辑放入service
		try {
			t = setTypeInspectionProperties(request,t);
			t.setCreateAt(new Date());  //create a new TypeInspection data
			t = typeInspectionService.save(t);
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
				//TODO deal with multiple sheets (Exception)
				Sheet contentSheet = workbook.getSheetAt(0);
				Long inspectionId = t.getId();
				int importRes = importContentSheet(contentSheet, inspectionId);
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
	 * 更新单条数据（包括报告详情文件）
	 */
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateInspection(@RequestBody MultipartFile file, HttpServletRequest request) {

		Map<String, Object> res = new LinkedHashMap<String, Object>();
		
		/* if null id or null entity */
		if(request.getParameter("id") == null ){
			res.put("code", RetCode.UPDATE_DB_ERROR_CODE);
			res.put("error", "no id to update, try to create one");
			return res;
		}
		Long inspectionId = Long.parseLong(request.getParameter("id"));
		TypeInspection t = typeInspectionService.getTypeInspectionById(inspectionId);
		if(t == null){
			res.put("code", RetCode.UPDATE_DB_ERROR_CODE);
			res.put("error", "no entity to update, try to create one");
			return res;
		}
		
		/* save request form data to typeInspection table */
		try {
			t = setTypeInspectionProperties(request,t);
			t = typeInspectionService.updateTypeInspection(request, t);
		} catch (Exception e) {
			logger.error("", e);
			res.put("error", e.getMessage());
			res.put("code", RetCode.UPDATE_DB_ERROR_CODE);
			return res;
		}
		
		/* parse and save excel file */
		if (null == file || file.isEmpty()) {
			res.put("code", RetCode.FILE_EMPTY_CODE);
			res.put("status", "No new file to update contents list");
		} else {
			InputStream xlsxFile = null;
			/* Test excel Encryption & handle exceptions */
			try {
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				Sheet contentSheet = workbook.getSheetAt(0);
				int importRes = importContentSheet(contentSheet, inspectionId);
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
	 * HttpServletRequest请求体属性存入TypeInspection实体，不包括创建时间
	 * @param request 前端提交的表单数据
	 * @param t 初始的TypeInspection实体，可为空（新建）或者含id（更新）
	 * @return t 转换后的TypeInspection实体，不包括创建时间
	 */
	private TypeInspection setTypeInspectionProperties(HttpServletRequest request, TypeInspection t) throws Exception{
		/* setAwardDate */
		long awardDateLong = Long.parseLong(request.getParameter("awardDate"))*1000;  
		Date awardDate = (new Date(awardDateLong));
		t.setAwardDate(awardDate);
		/* setDocFilename */
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
		
		try{
			MultipartFile file = multipartRequest.getFile("file");   
			String fileName = file.getOriginalFilename();
			t.setDocFilename(fileName);
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			/* set other properties */
			t.setModel(request.getParameter("model"));
			t.setName(request.getParameter("name"));
			t.setVersion(request.getParameter("version"));
			t.setTestType(request.getParameter("testType"));
			t.setCompany(request.getParameter("company"));
			t.setBasis(request.getParameter("basis"));
			t.setDocNo(request.getParameter("docNo"));
			t.setCertUrl(request.getParameter("certUrl"));
			t.setOrganization(request.getParameter("organization"));
			t.setRemarks(request.getParameter("remarks"));
			t.setOperator(request.getParameter("operator"));
			t.setUpdateAt(new Date());
			t.setOperator("TESTER");  //TODO 获取当前用户
		}
		return t;
	}

	/**
	 * 导入检测报告
	 * 
	 * @param contentSheet:
	 *            <检测项索引表>工作表
	 * @param inspectionId:  对应的型检id
	 * @return res: 0更新成功， 1更新失败：列表为空，2更新失败:缺少关键字
	 * @author langyicong
	 * @throws Exception
	 */
	private int importContentSheet(Sheet contentSheet, Long inspectionId) throws Exception {
		int res = 1;
		int rows = contentSheet.getLastRowNum() + 1;
		logger.debug("the total number of inspection content list is {}.", rows);
		List<InspectContent> contentList = new ArrayList<>(rows);

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
				if (Pattern.compile("(.*项目)|用例名称|功能列表").matcher(cellValue).find()) {
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
				c.setInspectionId(inspectionId);
				contentList.add(c);
			}
		}
		inspectContentService.importContentList(contentList, inspectionId);
		res = 0;
		return res;
	}
}
