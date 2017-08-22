package com.hikvision.ga.hephaestus.cert.repository.impl;

import com.hikvision.ga.hephaestus.cert.domain.InspectContent;
import com.hikvision.ga.hephaestus.cert.repository.InspectContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class InspectContentRepositoryImpl extends SimpleJpaRepository<InspectContent, Long>
        implements InspectContentRepository {

    @Autowired
    @PersistenceContext
    private EntityManager em;

    public InspectContentRepositoryImpl(EntityManager em) {
        super(InspectContent.class, em);
    }

    @SuppressWarnings("unchecked")
    public List<InspectContent> findByInspectionIdOrderById(Long inspectionId) {
        Query query = em.createQuery("from InspectContent where inspectionId=:inspectionId order by id");
        query.setParameter("inspectionId", inspectionId);
        return query.getResultList();
    }

    public InspectContent findOneByInspectionId(Long inspectionId) throws Exception {
        Query query = em.createQuery("from InspectContent where inspectionId=:inspectionId");
        query.setParameter("inspectionId", inspectionId);
        return (InspectContent) query.getSingleResult();
    }

}
