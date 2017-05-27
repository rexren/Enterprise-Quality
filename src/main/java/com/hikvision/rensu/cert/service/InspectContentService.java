package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.InspectContentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rensu on 17/4/28.
 */
@Service
public class InspectContentService {

    @Autowired
    private InspectContentRepository inspectContentRepository;
    
    public InspectContent get(Long id) {
        return inspectContentRepository.findOne(id);
    }
    
    public List<InspectContent> getContentsAll(Long inspectionId){
		return inspectContentRepository.findContentsByFK(inspectionId);
    }
    
    /**
    * 与数据库中的contentList比较，如果已经存在，则删除后插入新数据
    * @param contentList 待插入数据库的检测报告内容表（主控方列表）
    * @param t 检测条目（被控方）
    * @return void
    */
    @Transactional
    public void importContentList(List<InspectContent> contentList, TypeInspection t) throws Exception {
    	InspectContent c = new InspectContent();
    	if(t.getContents()!=null){
    		t.setContents(null);
    		Long id = t.getId();
    		List<InspectContent> list = inspectContentRepository.findContentsByFK(id);
    		inspectContentRepository.deleteInBatch(list);
    	}
    	//根据主表的ID，设置到子表的外键中，再保存子表
    	for(int i=0; i<contentList.size(); i++){
    		c = contentList.get(i);
    		c.setOwner(t);
    	}
    	inspectContentRepository.save(contentList);
    }
    
}
