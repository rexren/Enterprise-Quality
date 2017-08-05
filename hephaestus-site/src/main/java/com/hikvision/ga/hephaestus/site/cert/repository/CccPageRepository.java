package com.hikvision.ga.hephaestus.site.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.site.cert.domain.CccPage;
import com.hikvision.ga.hephaestus.site.cert.repository.base.BasicDAO;

@NoRepositoryBean
public interface CccPageRepository extends JpaRepository<CccPage, Long>, BasicDAO {

	List<CccPage> findByDocNo(String docNo);

	List<CccPage> searchCccByKeyword(String fieldName, String[] keywords);
}
