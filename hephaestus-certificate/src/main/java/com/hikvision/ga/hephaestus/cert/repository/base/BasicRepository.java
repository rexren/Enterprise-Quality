package com.hikvision.ga.hephaestus.cert.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BasicRepository继承自JpaRepository，保留Spring Data JPA的默认方法
 * 注解@NoRepositoryBean表示该接口不会创建这个接口的实例
 */
@NoRepositoryBean
public interface BasicRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	EntityManager getEntityManager();

}
