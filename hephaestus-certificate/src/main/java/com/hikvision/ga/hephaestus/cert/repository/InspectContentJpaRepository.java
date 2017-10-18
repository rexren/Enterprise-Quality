package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;


/**
 * Created by rensu on 17/4/28.
 */
@Repository
public interface InspectContentJpaRepository extends JpaRepository<InspectContent, Long> {

	/**
	 * @param inspectionId
	 * @return InspectContent列表
	 */
	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	//public InspectContent findOneByInspectionId(Long id) throws Exception;

}
