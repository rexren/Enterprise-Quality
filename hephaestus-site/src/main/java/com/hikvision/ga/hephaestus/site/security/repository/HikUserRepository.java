package com.hikvision.ga.hephaestus.site.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.site.security.domain.HikUser;

/**
 * @Description: UserRoleRepository
 * @author langyicong
 */
@Repository
public interface HikUserRepository extends JpaRepository<HikUser, Long> {

  List<HikUser> findByUserName(String userName);
  
}
