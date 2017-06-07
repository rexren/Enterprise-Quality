package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.TypeInspection;

/**
 * Created by rensu on 2017/4/22.
 */
@Repository
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long> {
	
	public List<TypeInspection> findByDocNo(String docNo);
}
