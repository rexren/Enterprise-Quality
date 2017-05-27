package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        /* 按照更新时间降序排序（从先到后） */
        Sort s = new Sort(Direction.DESC, "UpdateAt");
        Pageable page = new PageRequest(pn, ps, s);
        Page<TypeInspection> res = typeInspectionRepository.findAll(page);
        return res;
    }

    public TypeInspection save(TypeInspection typeInspection) {
        return typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection getTypeInspectionById(Long id) {
        return typeInspectionRepository.findOne(id);
    }

    public void deleteTypeInspection(TypeInspection t){
    	typeInspectionRepository.delete(t);
    }
   
    /**
    * 与数据库中的Inspections比较，如果docNo已经存在，则删除后插入新数据，否则直接使用save方法
    * @param inspections 带插入的List
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

}
