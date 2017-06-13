package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.base.BasicDAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rensu on 17/4/28.
 */
@NoRepositoryBean
public interface InspectContentRepository extends JpaRepository<InspectContent, Long>,BasicDAO{

	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId);

	public InspectContent findOneByInspectionId(Long id) throws Exception;

}
