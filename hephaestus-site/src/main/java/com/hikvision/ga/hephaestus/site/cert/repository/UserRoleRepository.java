package com.hikvision.ga.hephaestus.site.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	List<UserRole> getRoleByUserId(Long userId);

}
