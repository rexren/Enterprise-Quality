package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.TypeInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by rensu on 2017/4/22.
 */
@Repository
public interface TypeInspectionRepository extends JpaRepository<TypeInspection, Long> {


}
