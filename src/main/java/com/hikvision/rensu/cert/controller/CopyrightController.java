package com.hikvision.rensu.cert.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import com.hikvision.rensu.cert.constant.RetCode;
import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.service.CopyrightService;

@Controller
@RequestMapping("/copyright")
public class CopyrightController {
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

    @Autowired
    private CopyrightService copyrightService;
   
    /**
     * 获取双证列表页
     */
    @RequestMapping(value ="/list.do", method = RequestMethod.GET)
    @ResponseBody
    public Page<Copyright> getListByPage(Integer pageNum, Integer pageSize) {
        return copyrightService.getCopyrightByPage(pageNum, pageSize);
    }
    
    /**
     * 获取双证单条数据
     */
    @RequestMapping(value ="/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public Copyright getCopyright(Long id) {
        return copyrightService.getCopyrightById(id);
    }
    
    /**
     * 保存单条数据
     * TODO 自定义返回类型
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
	public List<String> saveCopyrightForm(HttpServletRequest request) {
    	System.out.println("Start"+request.getParameter("softwareName"));
        //TODO 保存数据
    	List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        return list;
    }
    

	/**
	 * 列表页(/inspections) 导入列表文件
	 */
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveCopyrightList(@RequestBody MultipartFile file) {
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
					if (Pattern.compile("双证").matcher(workbook.getSheetName(i)).find()) {
						System.out.println(workbook.getSheetName(i));
						Sheet sheet = workbook.getSheetAt(i);
						importCopyrightSheet(sheet);
						// TODO 导入双证解析
					} else {
						// TODO 错误：找不到sheet
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
	 * 软件名称|简称
	 * 软件著作权登记号|签发日期|软件著作权文件链接|认证/测试机构|软件类型|
	 * 软件产品登记证书|登记日期|登记有效期|软件产品登记证书链接|认证/测试机构|
	 * 软件评测报告|签发日期|软件评测报告链接|软件评测报告认证/测试机构|
	 * 类别界定报告|鉴定/界定日期|类别界定报告链接|类别界定报告认证/测试机构|软件型号|
	 * 负责人
	 * @param sheet:
	 *            【双证】工作薄
	 * @return res: 0更新成功， 1更新失败：列表为空，2更新失败:其他必填数据为空
	 * @author langyicong
	 * @throws Exception
	 */
	private int importCopyrightSheet(Sheet sheet) throws Exception {
		int res = 1;
		int rows = sheet.getLastRowNum() + 1;
		logger.debug("the total number of type inspection is {}.", rows);
		
		List<Copyright> crList = new ArrayList<>();
		//TODO row = 2 找到表头并且 判断表头是否符合条件
		for (int row = 2; row < rows; row++) {
			Row r = sheet.getRow(row);
			if (r.getCell(0) != null && r.getCell(0).getCellTypeEnum() != CellType.BLANK
					&& r.getCell(1).getCellTypeEnum() != CellType.BLANK) {
				Copyright c = new Copyright();
				c.setSoftwareName(r.getCell(0).getStringCellValue());
				c.setAbbreviation(r.getCell(1).getStringCellValue());
				c.setCrNo(r.getCell(2).getStringCellValue());
				if (r.getCell(3) != null) {
					if (r.getCell(3).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(3).setCellType(CellType.NUMERIC);
					}
					c.setCrDate(r.getCell(3).getDateCellValue());
				} else {
					res = 2;
					return res;
				}
				c.setCrUrl(r.getCell(4).getStringCellValue());
				c.setCrOrganization(r.getCell(5).getStringCellValue());
				c.setCrSoftwareType(r.getCell(6).getStringCellValue());
				c.setRgNo(r.getCell(7).getStringCellValue());
				if(r.getCell(8)!=null){
					if (r.getCell(8).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(8).setCellType(CellType.NUMERIC);
					}
					c.setRgDate(r.getCell(8).getDateCellValue());
				}	
				if(r.getCell(9)!=null){
					if (r.getCell(9).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(9).setCellType(CellType.NUMERIC);
					}
					c.setRgExpiryDate(r.getCell(9).getDateCellValue());
				}
				c.setRgUrl(r.getCell(10).getStringCellValue());
				c.setRgOrganization(r.getCell(11).getStringCellValue());
				c.setEpNo(r.getCell(12).getStringCellValue());
				if(r.getCell(13)!=null){
					if (r.getCell(13).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(13).setCellType(CellType.NUMERIC);
					}
					c.setEpDate(r.getCell(13).getDateCellValue());
				}
				c.setEpUrl(r.getCell(14).getStringCellValue());
				c.setEpOrganization(r.getCell(15).getStringCellValue());
				c.setCdNo(r.getCell(16).getStringCellValue());
				if(r.getCell(17)!=null){
					if (r.getCell(17).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(17).setCellType(CellType.NUMERIC);
					}
					c.setCdDate(r.getCell(17).getDateCellValue());
				}
				c.setCdUrl(r.getCell(18).getStringCellValue());
				c.setCdOrganization(r.getCell(19).getStringCellValue());
				c.setModel(r.getCell(20).getStringCellValue());
				c.setCharge(r.getCell(21).getStringCellValue());
				c.setOperator("TESTER");
				//c.setCreateDate(createDate);
				c.setCreateDate(new Date());
				c.setUpdateDate(new Date());
				crList.add(c);
			}
		}
		//TODO 写入数据库
		return res;
	}

    
    
}
