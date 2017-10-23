package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;

/**
 * @Description: 检测报告内容
 * @author langyicong
 * @date 2017年10月23日 
 * @modify {原因} by langyicong 2017年10月23日
 */
@Repository
public interface InspectContentRepository extends JpaRepository<InspectContent, Long> {

	/**
	 * @param inspectionId
	 * @return InspectContent列表
	 */
	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);

}
