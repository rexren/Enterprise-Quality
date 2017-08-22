package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.cert.domain.CccPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface CccPageRepository extends JpaRepository<CccPage, Long> {

	List<CccPage> findByDocNo(String docNo);

	List<CccPage> searchCccByKeyword(String fieldName, String[] keywords);
}
