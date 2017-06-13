package com.hikvision.rensu.cert.repository;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.repository.base.BasicDAO;
import com.hikvision.rensu.cert.repository.base.BasicRepository;

@NoRepositoryBean
public interface CopyrightRepository extends JpaRepository<Copyright, Long>, BasicDAO{

	List<Copyright> findBySoftwareName(String softwareName);
	
	List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords);
}
