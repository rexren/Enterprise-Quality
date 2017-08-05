package com.hikvision.rensu.cert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikvision.rensu.cert.domain.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

	public List<SystemUser> findByName(String userName);
}
