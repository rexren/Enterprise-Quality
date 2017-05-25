package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
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

    public void deleteTypeInspection(TypeInspection t){
    	typeInspectionRepository.delete(t);
    }
    
    /**
    * 与数据库中的Inspections比较，如果docNo已经存在，则删除后插入新数据，否则直接使用save方法
    * @param inspections 有序表
    * @return void
    */
    public void importInspectionList(List<TypeInspection> inspections){
    	for(int i = 0; i < inspections.size(); i++){
    		List<TypeInspection> dupItems = typeInspectionRepository.findByDocNo(inspections.get(i).getDocNo());
    		if(dupItems.size()>0){
    			/* delete and insert new one */
    			typeInspectionRepository.delete(dupItems);
        	} 
    		typeInspectionRepository.save(inspections.get(i));
    	}
    }
   
    /**
     * 从表单新建单条TypeInspection
     */
    public TypeInspection saveSingleTypeInspection(HttpServletRequest request) throws Exception{
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
        return typeInspectionRepository.save(t);
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
    	
}
