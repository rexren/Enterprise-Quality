package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.CccPage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CccPageRepository extends JpaRepository<CccPage, Long>{

	public List<CccPage> findByDocNo(String docNo);
}