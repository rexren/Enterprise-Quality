package com.hikvision.rensu.cert.controller;


import com.hikvision.rensu.cert.constant.RetCode;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.InspectContentService;
import com.hikvision.rensu.cert.service.TypeInspectionService;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.LinkedHashMap;
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
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public Page<TypeInspection> getInspecionListByPage(Integer pageNum, Integer pageSize) {
        return typeInspectionService.getInspectionByPage(pageNum, pageSize);
    }

    /**
     * 获取单条数据
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
            try {
            	xlsxFile = file.getInputStream();
            } catch (IOException e) {
            	logger.error("",e);
                res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	return res;
            }
            
            /*
             * Test excel Encryption & handle exceptions
             */
            try {
                Workbook workbook = WorkbookFactory.create(xlsxFile);
                int sheetCount = workbook.getNumberOfSheets();
                for(int i=0; i<sheetCount; i++){
                	if(Pattern.compile("公检").matcher(workbook.getSheetName(i)).find()){
                		System.out.println(workbook.getSheetName(i));
                		Sheet sheet = workbook.getSheetAt(i);
                		typeInspectionService.importInspectionList(sheet);
                	} 
                	else if(Pattern.compile("双证").matcher(workbook.getSheetName(i)).find()){
                		System.out.println(workbook.getSheetName(i));
                		//TODO 导入双证解析
                	}
                	else if(Pattern.compile("3C").matcher(workbook.getSheetName(i)).find()){
                		System.out.println(workbook.getSheetName(i));
                		//TODO 导入3C解析
                	}
                	else if(Pattern.compile("更新说明").matcher(workbook.getSheetName(i)).find()){
                		System.out.println(workbook.getSheetName(i));
                		//TODO 导入更新说明
                	}
                }
                res.put("code", RetCode.SUCCESS_CODE);
            } catch (IOException e) {
            	String errMessage = e.getMessage();
            	logger.error("",e);
            	res.put("error", e.getMessage());
            	String regExp = "EncryptionInfo";
            	if(Pattern.compile(regExp).matcher(errMessage).find()){
            		res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
            	} else{
            		res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	}
            	return res;
            } catch (EncryptedDocumentException | InvalidFormatException e) {
            	res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	logger.error("",e);
            	return res;
			}
            
        }
        return res;
    }

    /**
     * 新建保存单条数据（包括报告详情文件）
     * @param file 表单文件 
     * @param request 表单内容
     * @return res Map->Json 返回状态
     * @author langyicong
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveInspectionForm(@RequestBody MultipartFile file, HttpServletRequest request) {
        Map<String, Object> res = new LinkedHashMap<String, Object>();

    	/* save request form data to typeInspection table */
        try {
			typeInspectionService.saveSingleTypeInspection(request);
		} catch (Exception e) {
			logger.error("",e);
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
            try {
            	xlsxFile = file.getInputStream();
            } catch (IOException e) {
            	logger.error("",e);
            	res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	return res;
            }
            
            /* Test excel Encryption & handle exceptions */
            try {
                Workbook workbook = WorkbookFactory.create(xlsxFile);
                // 解析详细检测报告索引文件
                inspectContentService.saveInspectContents(workbook);
                res.put("code", RetCode.SUCCESS_CODE);
            } catch (IOException e) {
            	String errMessage = e.getMessage();
            	logger.error("",e);
            	res.put("error", e.getMessage());
            	String regExp = "EncryptionInfo";
            	if(Pattern.compile(regExp).matcher(errMessage).find()){
            		res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
            	} else{
            		res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	}
            	return res;
            } catch (EncryptedDocumentException | InvalidFormatException e) {
            	res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	logger.error("",e);
            	return res;
			} 
        }
        return res;
    }

    /**
     * 更新单条数据（包括报告详情文件）
     * TODO delete and then insert
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateInspection(@RequestBody MultipartFile file, HttpServletRequest request) {
    	//TODO request.getParameter("id") NOT NULL
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
        	//TODO 比较和更新excel工作簿文件
        	String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.debug("the upload file name is {}.", fileName + suffixName);
            
	    	/* 读取文件流*/
            InputStream xlsxFile = null;
            try {
            	xlsxFile = file.getInputStream();
            } catch (IOException e) {
                res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	logger.error("",e);
            }
            
            /**
             * Test excel Encryption & handle exceptions
             */
            try {
                Workbook workbook = WorkbookFactory.create(xlsxFile);
                //TODO 解析检测报告的excel文档
                res.put("code", RetCode.SUCCESS_CODE);
            } catch (IOException e) {
            	String errMessage = e.getMessage();
            	logger.error("",e);
            	res.put("error", e.getMessage());
            	String regExp = "EncryptionInfo";
            	if(Pattern.compile(regExp).matcher(errMessage).find()){
            		res.put("code", RetCode.FILE_ENCYPTED_ERROR_CODE);
            	} else{
            		res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	}
            } catch (EncryptedDocumentException | InvalidFormatException e) {
            	res.put("error", e.getMessage());
            	res.put("code", RetCode.FILE_PARSING_ERROR_CODE);
            	logger.error("",e);
			} 
        }
        
        return res;
    }
    
}
