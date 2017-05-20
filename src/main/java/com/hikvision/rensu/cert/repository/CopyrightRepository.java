package com.hikvision.rensu.cert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.Copyright;

@Repository
public interface CopyrightRepository extends JpaRepository<Copyright, Long>{

}
