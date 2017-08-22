package com.hikvision.ga.hephaestus.site.security;

import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.domain.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

	public List<SystemUser> findByName(String userName);
}
