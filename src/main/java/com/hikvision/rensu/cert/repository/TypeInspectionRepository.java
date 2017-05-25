package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.TypeInspection;

/**
 * Created by rensu on 2017/4/22.
 */
@Repository
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long> {
	
	//TODO 完善更新
	@Query("select t from TypeInspection t where t.docNo=:no")
	public List<TypeInspection> findByDocNo(@Param("no")String docNo);
}
