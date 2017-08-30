package com.hikvision.ga.hephaestus.site.security.service;

import com.hikvision.ga.hephaestus.site.security.domain.SystemUser;
import com.hikvision.ga.hephaestus.site.security.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

/**
 * @author langyicong
 *
 */
@Service
@Transactional
public class SystemUserService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    /**
     * 通过用户名查找用户
     * @param userName
     * @return 查找到的用户列表
     */
    public List<SystemUser> findByName(String userName) {
        return systemUserRepository.findByName(userName);
    }

    /**
     * 通过Spring Security获取当前用户名称
     * @return 用户名称
     */
    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }
        return String.valueOf(principal);
    }

}
