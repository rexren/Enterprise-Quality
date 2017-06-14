package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.repository.base.BasicDAO;

@NoRepositoryBean
public interface CopyrightRepository extends JpaRepository<Copyright, Long>, BasicDAO{

	List<Copyright> findBySoftwareName(String softwareName);
	
	List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords);
}
