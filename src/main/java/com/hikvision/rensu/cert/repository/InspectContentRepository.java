package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.InspectContent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by rensu on 17/4/28.
 */
@Repository
public interface InspectContentRepository extends JpaRepository<InspectContent, Long> {

	/*	TODO debug: org.springframework.http.converter.HttpMessageNotWritableException: 
		Could not write content: Infinite recursion (StackOverflowError) */
	@Query("select c from InspectContent c where c.owner.id=:fk")
	public List<InspectContent> findContentsByFK(@Param("fk")Long inspectionId);
	
}
