package com.hikvision.rensu.cert.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.InspectContentRepository;

@Repository
public class InspectContentRepositoryImpl extends SimpleJpaRepository<InspectContent, Long> implements InspectContentRepository {
	
	@Autowired
	public InspectContentRepositoryImpl(EntityManager em) {
		super(InspectContent.class, em);
	}

	@SuppressWarnings("unchecked")
	public List<InspectContent> findByInspectionIdOrderById(Long inspectionId) {
		Query query = getEntityManager().createQuery("from InspectContent where inspectionId=:inspectionId order by id");
		query.setParameter("inspectionId", inspectionId);
		return query.getResultList();
	}

	public InspectContent findOneByInspectionId(Long inspectionId) throws Exception{
		Query query = getEntityManager().createQuery("from InspectContent where inspectionId=:inspectionId");
		query.setParameter("inspectionId", inspectionId);
		return (InspectContent) query.getSingleResult();
	}

}
