package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hikvision.rensu.cert.domain.CccPage;

public interface CccPageRepositoryCustom  {

	//自定义查询
    public Page<CccPage> searchCccByKeyword(String fieldName,String likePattern, Pageable page);
    
    List<CccPage> findByDocNo(String stringCellValue);

}
