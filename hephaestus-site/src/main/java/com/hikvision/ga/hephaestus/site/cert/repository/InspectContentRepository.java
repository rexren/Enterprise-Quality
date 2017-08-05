package com.hikvision.ga.hephaestus.site.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.domain.InspectContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.hikvision.ga.hephaestus.site.cert.repository.base.BasicDAO;

/**
 * Created by rensu on 17/4/28.
 */
@NoRepositoryBean
public interface InspectContentRepository extends JpaRepository<InspectContent, Long>,BasicDAO{

	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);

	public InspectContent findOneByInspectionId(Long id) throws Exception;

}
