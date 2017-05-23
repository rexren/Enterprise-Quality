package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.InspectContentRepository;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by rensu on 17/4/21.
 */
@Service
@Transactional
public class TypeInspectionService {

    private final static Logger logger = LoggerFactory.getLogger(TypeInspectionService.class);

    @Autowired
    private TypeInspectionRepository typeInspectionRepository;

    @Autowired
    private InspectContentRepository inspectContentRepository;

    public Page<TypeInspection> getInspectionByPage(Integer pageNum, Integer pageSize) {
        int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
        int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
        Pageable page = new PageRequest(pn, ps);
        return typeInspectionRepository.findAll(page);
    }

    public void save(TypeInspection typeInspection) {
        typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection getTypeInspectionById(Long id) {
        return typeInspectionRepository.findOne(id);
    }

    /**
     * 导入列表数据
     * | 产品型号 | 软件名称 | 软件版本 | 测试／检验类别 | 受检单位 | 测试依据 | 颁发日期 | 文件编号 | 证书系统链接 | 认证／测试机构 | 备注
     * @param sheet 【公检&国标】工作薄
     * @return void
     * @author langyicong
     * @throws Exception
     */
    //TODO define a class importInspectionListException extends Exception & test internal format of the sheet(eg. the first row cell values)
    public void importInspectionList(Sheet sheet) throws Exception {
        int rows = sheet.getLastRowNum() - 2;
        logger.debug("the total number of type inspection is {}.", rows);

        for (int row = 2; row < rows; row++) {
            Row r = sheet.getRow(row);
            /* rows those are not empty */
            if(r.getCell(0)!=null && r.getCell(0).getCellTypeEnum()!=CellType.BLANK 
            		&& r.getCell(1).getCellTypeEnum()!=CellType.BLANK){

                if(r.getCell(7)!=null){
                	/* test if DocNo is already in the database */
            		if(r.getCell(7).getCellTypeEnum()!=CellType.STRING){
            			r.getCell(7).setCellType(CellType.STRING);
            		}
            		List<TypeInspection> inspects = typeInspectionRepository.findByDocNo(r.getCell(7).getStringCellValue());
            		if(inspects.size()>0){
                		/* delete and insert new one */
            			typeInspectionRepository.delete(inspects);
                	}
                	TypeInspection t = new TypeInspection();
                    t.setModel(r.getCell(0).getStringCellValue());
                	t.setName(r.getCell(1).getStringCellValue());
                	t.setVersion(r.getCell(2).getStringCellValue());
                	t.setTestType(r.getCell(3).getStringCellValue());
                	t.setCompany(r.getCell(4).getStringCellValue());
                	t.setBasis(r.getCell(5).getStringCellValue());
                	if(r.getCell(6)!=null){
                		if(r.getCell(6).getCellTypeEnum()!=CellType.NUMERIC){
                			r.getCell(6).setCellType(CellType.NUMERIC);
                		}
                		t.setAwardDate(r.getCell(6).getDateCellValue());
                	} else{
                		/* handle with empty cell error */
                	}
                	if(r.getCell(7)!=null){
                		if(r.getCell(7).getCellTypeEnum()!=CellType.STRING){
                			r.getCell(7).setCellType(CellType.STRING);
                		}
                		t.setDocNo(r.getCell(7).getStringCellValue());
                	} else{
                		/* handle with empty cell error */
                	}
                	t.setCertUrl(r.getCell(8).getStringCellValue());
                	t.setOrganization(r.getCell(9).getStringCellValue());
                	t.setRemarks(r.getCell(10).getStringCellValue());
                	t.setCreateAt(new Date()); 
                	t.setUpdateAt(new Date());
                	t.setOperator("TESTER");  //TODO 获取当前用户
                	typeInspectionRepository.save(t);
            	} else{
            		/* handle with empty cell error */
            	}
                
            }
        }
    }
    
    /**
     * 从表单新建单条TypeInspection
     */
    public void saveSingleTypeInspection(HttpServletRequest request) throws Exception{
    	TypeInspection t = new TypeInspection();
    	if(request.getParameter("awardDate")!="0"){
            long awardDateLong = Long.parseLong(request.getParameter("awardDate"))*1000;  
            Date awardDate = (new Date(awardDateLong));
            t.setAwardDate(awardDate);
    	}
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
        t.setCreateAt(new Date());  //create a new TypeInspection data
        t.setUpdateAt(new Date());
        t.setOperator("TESTER");  //TODO 获取当前用户
        typeInspectionRepository.save(t);
    }
    
    /**
     * 更新单条TypeInspection
     */
    public void updateTypeInspection(HttpServletRequest request) throws Exception{
    	TypeInspection t = new TypeInspection();
        long awardDateLong = Long.parseLong(request.getParameter("awardDate"))*1000;  
        Date awardDate = (new Date(awardDateLong));
        t.setModel(request.getParameter("model"));
        t.setName(request.getParameter("name"));
        t.setVersion(request.getParameter("version"));
        t.setTestType(request.getParameter("testType"));
        t.setCompany(request.getParameter("company"));
        t.setBasis(request.getParameter("basis"));
        t.setAwardDate(awardDate);
        t.setDocNo(request.getParameter("docNo"));
        t.setCertUrl(request.getParameter("certUrl"));
        t.setOrganization(request.getParameter("organization"));
        t.setRemarks(request.getParameter("remarks"));
        t.setOperator(request.getParameter("operator"));
        t.setUpdateAt(new Date());
        t.setOperator("TESTER");  //TODO 获取当前用户
        //typeInspectionRepository.updateTypeInspectionRepo(request.getParameter("id"), t);
        typeInspectionRepository.save(t);
    }
    	
    /*
    public void importTypeContent(InputStream xlsxFile) {

        try {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsxFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表
            for (int i = 0; i < sheetCount; i++) {

                InspectContent content = new InspectContent();

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
                        //data put into content
                    }
                }
                inspectContentRepository.save(content);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
    }*/

}
