package com.hikvision.ga.hephaestus.site.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.ga.hephaestus.site.security.domain.UserRole;

/**
 * @Description: UserRoleRepository
 * @author langyicong
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  List<UserRole> getRoleByUserId(Long userId);

}
