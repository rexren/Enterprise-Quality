package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.repository.InspectContentRepository;

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
    
    /**
    * 用外键查询contents
    * @param inspectionId 外键id
    * @return List<InspectContent> 查到的内容条目列表
    */
    public List<InspectContent> getContentsAll(Long inspectionId){
		return inspectContentRepository.findContentsByFK(inspectionId);
    }
    
    /**
    * 用外键删除contents
    * @param inspectionId 外键id
    * @return List<InspectContent> 查到的内容条目列表
    */
	public void deleteByFK(Long inspectionId) {
		List<InspectContent> list = getContentsAll(inspectionId);
		inspectContentRepository.deleteInBatch(list);
	}
    
    
    /**
    * 与数据库中的contentList比较，如果已经存在，则删除后插入新数据
    * @param contentList 待插入数据库的检测报告内容表（子表）
    * @param inspectionId 检测条目（主表条目）的id
    * @return void
    */
    @Transactional
    public void importContentList(List<InspectContent> contentList, Long inspectionId) throws Exception {
		// if there exists entries of this inspectionId in InspectContent table, delete these entries
   		if(getContentsAll(inspectionId)!=null){
   			deleteByFK(inspectionId);
    	}
    	inspectContentRepository.save(contentList);
    }
}
