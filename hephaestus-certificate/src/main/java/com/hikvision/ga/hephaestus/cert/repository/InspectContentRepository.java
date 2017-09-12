package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.cert.InspectContent;

/**
 * Created by rensu on 17/4/28.
 */
@NoRepositoryBean
public interface InspectContentRepository extends JpaRepository<InspectContent, Long> {

	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);

	public InspectContent findOneByInspectionId(Long id) throws Exception;

}
