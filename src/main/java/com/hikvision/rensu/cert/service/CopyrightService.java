package com.hikvision.rensu.cert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.repository.CopyrightRepository;

@Service
public class CopyrightService {
	@Autowired
	private CopyrightRepository copyrightRepository;
	
	public Page<Copyright> getCopyrightByPage(Integer pageNum, Integer pageSize){
    	int pn = pageNum == null?0:pageNum.intValue()-1;
    	int ps = pageSize ==null?20:pageSize.intValue(); // 默认20条/页
        Pageable page = new PageRequest(pn, ps);
        return copyrightRepository.findAll(page);
	}

	public Copyright getCopyrightById(Long id){
		return copyrightRepository.findOne(id);
	}
}
