package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.base.BasicDAO;
import com.hikvision.rensu.cert.support.typeSearchResult;

/**
 * Created by rensu on 2017/4/22.
 */
@NoRepositoryBean
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long>, BasicDAO{
	
	public List<TypeInspection> findByDocNo(String docNo);

	/**
	 * 关键字联合查询，并且按照命中率排序
	 * */
	public List<typeSearchResult> joinSearchTypeInspection(String fieldName, String[] keywordList, String[] contentKeywordList);
	
}
