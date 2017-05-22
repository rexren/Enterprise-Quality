package com.hikvision.rensu.cert.repository;

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
	
	/**
	 * TODO Update Query need to be modified
	 */
	@Modifying(clearAutomatically = true)
	@Query("update TypeInspection set TypeInspection =:item where t.id =:inspectionId")
	public void updateTypeInspectionRepo(@Param("inspectionId") String id, @Param("item") TypeInspection t);
}
