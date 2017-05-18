package com.hikvision.rensu.cert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.CopyRight;

@Repository
public interface CopyRightRepository extends JpaRepository<CopyRight, Long>{

}
