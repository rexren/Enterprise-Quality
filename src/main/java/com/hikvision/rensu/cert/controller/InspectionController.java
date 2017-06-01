package com.hikvision.rensu.cert.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hikvision.rensu.cert.constant.RetStatus;
import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.InspectContentService;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import com.hikvision.rensu.cert.support.AjaxResult;
import com.hikvision.rensu.cert.support.BaseResult;
import com.hikvision.rensu.cert.support.ListContent;
import com.hikvision.rensu.cert.support.ListResult;

/**
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

	private final static String SORT_TYPEINSPECTION_UPDATEAT = "UpdateAt";

	@Autowired
	private TypeInspectionService typeInspectionService;

	@Autowired
	private InspectContentService inspectContentService;

	/**
	 * 获取公检&国标文件列表页
	 * 
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            页大小 默认20条/页
	 * @param sortBy
	 *            需要倒序排序的字段，默认为更新时间UpdateAt字段
	 * @param direction
	 *            <=0表示降序，>0为升序，默认为降序
	 * @return 包含型检数据对象的返回对象
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ListResult getInspecionListByPage(Integer pageNum, Integer pageSize, String sortBy, Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORT_TYPEINSPECTION_UPDATEAT; // 默认按照更新时间倒序
		}
		ListResult res = new ListResult();
		try {
			Page<TypeInspection> p = typeInspectionService.getInspectionByPage(pn, ps, sortBy, dir);
			res.setListContent(new ListContent<TypeInspection>(p.getSize(), p.getTotalElements(), p.getTotalPages(),
					p.getContent()));
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e.getMessage());
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}
		return res;
	}

	/**
	 * 获取单条型检数据（不包括列表）
	 * 
	 * @param id
	 *            型检id
	 * @return 包含型检数据对象的返回对象
	 */
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult<TypeInspection> getInspecionById(Long id) {
		AjaxResult<TypeInspection> res = new AjaxResult<TypeInspection>();
		if (null == id) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
		} else {
			try {
				TypeInspection t = typeInspectionService.getTypeInspectionById(id);
				if (t != null) {
					res.setData(t);
					res.setCode(RetStatus.SUCCESS.getCode());
					res.setMsg(RetStatus.SUCCESS.getInfo());
				} else {
					res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
					res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				}
			} catch (Exception e) {
				logger.error("", e.getMessage());
				res.setCode(RetStatus.SYSTEM_ERROR.getCode());
				res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			}
		}
		return res;
	}

	/**
	 * 获取单条型检数据的报告内容列表
	 * 
	 * @param id
	 *            型检id
	 * @return 包含报告内容列表的返回对象
	 */
	@RequestMapping(value = "/contents.do", method = RequestMethod.GET)
	@ResponseBody
	public ListResult getContentsByInspectionId(Long id) {
		ListResult res = new ListResult();
		if (null == id) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
		} else {
			try {
				List<InspectContent> contents = inspectContentService.getContentsByInspectionId(id);
				if (contents != null && !contents.isEmpty()) {
					res.setListContent(
							new ListContent<InspectContent>(contents.size(), (long) contents.size(), 1, contents));
					res.setCode(RetStatus.SUCCESS.getCode());
					res.setMsg(RetStatus.SUCCESS.getInfo());
				} else {
					res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
					res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				}
			} catch (Exception e) {
				logger.error("", e.getMessage());
				res.setCode(RetStatus.SYSTEM_ERROR.getCode());
				res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			}
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
	public BaseResult saveInspectionForm(@RequestBody MultipartFile file, HttpServletRequest request) {
		BaseResult res = new BaseResult();
		/* 表单验证:关键信息不为空 */
		if (StringUtils.isBlank(request.getParameter("model")) || StringUtils.isBlank(request.getParameter("name"))
				|| StringUtils.isBlank(request.getParameter("awardDate"))|| StringUtils.isBlank(request.getParameter("docNo"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}
		/* 表单验证:docNo不能重复 */
		//TODO 去掉空格后比较字符串StringUtils.trim
		if(typeInspectionService.findByDocNo(request.getParameter("docNo")).size()>0){
			res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
			res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
			return res;
		}

		/* parse excel file and save item */
		TypeInspection t = new TypeInspection();
		InputStream xlsxFile = null;
		List<InspectContent> contentlist = null;
		try {
			/* 页面参数组装成TypeInspection实体 */
			t = setTypeInspectionProperties(request, t);
			if (file != null && !file.isEmpty()) {
				/* Test excel Encryption & handle exceptions */
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				Sheet contentSheet = workbook.getSheetAt(0);
				contentlist = importContentSheet(contentSheet, t.getId());
			}
			typeInspectionService.saveTypeInspection(t, contentlist);
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (IOException e) {
			logger.error("", e);
			if (StringUtils.contains(e.getMessage(), "EncryptionInfo")) {
				res.setCode(RetStatus.FILE_ENCYPTED.getCode());
				res.setMsg(RetStatus.FILE_ENCYPTED.getInfo());
			} else {
				res.setCode(RetStatus.FILE_PARSING_ERROR.getCode());
				res.setMsg(RetStatus.FILE_PARSING_ERROR.getInfo());
			}
			return res;
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			logger.error("", e);
			res.setCode(RetStatus.FILE_INVALID.getCode());
			res.setMsg(RetStatus.FILE_INVALID.getInfo());
			return res;
		} catch (IllegalArgumentException e) {
			res.setCode(RetStatus.FILE_SHEET_MISSING.getCode());
			res.setMsg(RetStatus.FILE_SHEET_MISSING.getInfo());
			return res;
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			return res;
		} finally {
			IOUtils.closeQuietly(xlsxFile);
		}
		return res;
	}

	/**
	 * 更新单条数据（包括报告详情文件）
	 */
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInspection(@RequestBody MultipartFile file, HttpServletRequest request) {
		BaseResult res = new BaseResult();
		/* if null id or null entity */
		if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}
		//TODO docNo不能和除这条id对应的docNo之外的条目重复
		
		/* 查询对应的实体，并且将页面数据更新实体内容 */
		Long inspectionId = NumberUtils.toLong(request.getParameter("id"));
		TypeInspection t = null;
		InputStream xlsxFile = null;
		try {
			t = typeInspectionService.getTypeInspectionById(inspectionId);
			if (t == null) {
				res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
				res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				return res;
			}
			t = setTypeInspectionProperties(request, t); // 更新实体，包括文件名docFileName
			List<InspectContent> contentlist = null;
			if (file != null && !file.isEmpty()) {
				/* Test excel Encryption & handle exceptions */
				xlsxFile = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(xlsxFile);
				Sheet contentSheet = workbook.getSheetAt(0);
				contentlist = importContentSheet(contentSheet, inspectionId);
			}
			typeInspectionService.updateTypeInspection(t, contentlist);
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (IOException e) {
			logger.error("", e);
			if (StringUtils.contains(e.getMessage(), "EncryptionInfo")) {
				res.setCode(RetStatus.FILE_ENCYPTED.getCode());
				res.setMsg(RetStatus.FILE_ENCYPTED.getInfo());
			} else {
				res.setCode(RetStatus.FILE_PARSING_ERROR.getCode());
				res.setMsg(RetStatus.FILE_PARSING_ERROR.getInfo());
			}
			return res;
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			logger.error("", e);
			res.setCode(RetStatus.FILE_INVALID.getCode());
			res.setMsg(RetStatus.FILE_INVALID.getInfo());
			return res;
		} catch (IllegalArgumentException e) {
			res.setCode(RetStatus.FILE_SHEET_MISSING.getCode());
			res.setMsg(RetStatus.FILE_SHEET_MISSING.getInfo());
			return res;
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			return res;
		} finally {
			IOUtils.closeQuietly(xlsxFile);
		}
		return res;
	}

	/**
	 * 将HttpServletRequest请求体（页面参数）组装成TypeInspection实体
	 * 
	 * @param request
	 *            页面表单数据
	 * @param t
	 *            初始的TypeInspection实体，可为空（新建）或者含id（更新）
	 * @return t 转换后的TypeInspection实体，不包括创建时间
	 */
	private TypeInspection setTypeInspectionProperties(HttpServletRequest request, TypeInspection t) throws Exception {
		if (null == t) {
			t = new TypeInspection();
		}
		/* 传入的是新建的实体 */
		if (null == t.getId() || t.getId() <= 0) {
			t.setCreateAt(new Date());
		}
		/* setAwardDate */
		long awardDateLong = NumberUtils.toLong(request.getParameter("awardDate")) * 1000;
		Date awardDate = (new Date(awardDateLong));
		t.setAwardDate(awardDate);
		/* setDocFilename */
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		try {
			MultipartFile file = multipartRequest.getFile("file");
			t.setDocFilename((null == file) ? "" : file.getOriginalFilename());
		} catch (Exception e) {
			throw e;
		} finally {
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
			t.setOperator("TESTER"); // TODO 获取当前用户
		}
		return t;
	}

	/**
	 * 导入检测报告工作表到list中
	 * 
	 * @param contentSheet:
	 *            <检测项索引表>工作表
	 * @param inspectionId:
	 *            对应的型检id
	 * @return list 检测项列表
	 * @author langyicong
	 * @throws Exception
	 */
	private List<InspectContent> importContentSheet(Sheet contentSheet, Long inspectionId) throws Exception {
		int rows = contentSheet.getLastRowNum() + 1;
		logger.debug("the total number of inspection content list is {}.", rows);
		List<InspectContent> contentList = new ArrayList<>(); // 此处不能将row的值作为List的长度，因为工作表中可能会有无内容的行

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
					String cellValue = r.getCell(col).getStringCellValue();
					if (StringUtils.contains(cellValue, "序号")) {
						headRow = row;
						caseIdCol = col;
					}
					if (StringUtils.containsAny(cellValue, "项目", "用例名称", "功能列表")) {
						caseNameCol = col;
					}
					if (StringUtils.containsAny(cellValue, "要求", "用例说明", "功能描述")) {
						caseDescrCol = col;
					}
					if (StringUtils.contains(cellValue, "测试结果")) {
						testResultCol = col;
					}
				}
			}
			if (row == headRow) {
				break;
			}
		}

		if (caseIdCol < 0 || caseNameCol < 0 || caseDescrCol < 0) {
			throw new Exception("缺少关键字");
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
		return contentList;
	}
}
