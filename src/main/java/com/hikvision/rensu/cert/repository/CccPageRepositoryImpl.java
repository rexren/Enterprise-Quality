package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.CccPage;

@Repository
public class CccPageRepositoryImpl implements CccPageRepositoryCustom {

	@Override
	public Page<CccPage> searchCccByKeyword(String fieldName, String likePattern, Pageable page) {
		return null;
	}

	@Override
	public List<CccPage> findByDocNo(String stringCellValue) {
		// TODO Auto-generated method stub
		return null;
	}


}
