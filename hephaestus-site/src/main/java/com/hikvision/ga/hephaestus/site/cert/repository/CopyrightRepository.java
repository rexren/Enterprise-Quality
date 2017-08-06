package com.hikvision.ga.hephaestus.site.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.cert.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.site.cert.repository.base.BasicDAO;

@NoRepositoryBean
public interface CopyrightRepository extends JpaRepository<Copyright, Long>, BasicDAO{

	List<Copyright> findBySoftwareName(String softwareName);
	
	List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords);
}
