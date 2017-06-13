package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.base.BasicDAO;

/**
 * Created by rensu on 2017/4/22.
 */
@NoRepositoryBean
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long>, BasicDAO{
	
	public List<TypeInspection> findByDocNo(String docNo);

	public List<?> joinSearchTypeInspection(String fieldName, String[] keywordList, String[] contentKeywordList);
	
}
