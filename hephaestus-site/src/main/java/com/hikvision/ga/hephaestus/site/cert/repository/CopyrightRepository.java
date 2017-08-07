package com.hikvision.ga.hephaestus.site.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.cert.Copyright;

@NoRepositoryBean
public interface CopyrightRepository extends JpaRepository<Copyright, Long> {

	List<Copyright> findBySoftwareName(String softwareName);
	
	List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords);
}
