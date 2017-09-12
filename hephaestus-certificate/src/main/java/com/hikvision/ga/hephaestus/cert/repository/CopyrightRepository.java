package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.cert.domain.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface CopyrightRepository extends JpaRepository<Copyright, Long> {

	List<Copyright> findBySoftwareName(String softwareName);
	
	List<Copyright> searchCopyrightByKeyword(String fieldName, String[] keywords);
}
