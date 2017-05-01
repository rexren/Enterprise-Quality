package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.InspectContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by rensu on 17/4/28.
 */
@Repository
public interface InspectContentRepository extends JpaRepository<InspectContent, Long> {
}
