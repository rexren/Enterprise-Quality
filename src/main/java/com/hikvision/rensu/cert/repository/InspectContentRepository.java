package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.InspectContent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by rensu on 17/4/28.
 */
@Repository
public interface InspectContentRepository extends JpaRepository<InspectContent, Long> {

	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);
	
}
