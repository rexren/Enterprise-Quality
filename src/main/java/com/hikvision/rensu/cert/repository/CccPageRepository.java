package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.CccPage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CccPageRepository extends JpaRepository<CccPage, Long>{

	public List<CccPage> findByDocNo(String docNo);

//	@Query(value="select c from CccPage c where :fieldName like :keywords")
//	public Page<CccPage> searchCccByKeyword(@Param("fieldName")String fieldName, @Param("keywords")String keywords, Pageable page);

//	public Page<CccPage> searchCccByMyKeyword(String fieldName,String keywords, Pageable page);
	
}