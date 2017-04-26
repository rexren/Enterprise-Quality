package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.TypeInspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by rensu on 2017/4/22.
 */
public interface TypeInspectionRepository extends JpaRepository<TypeInspection,Long>{


}
