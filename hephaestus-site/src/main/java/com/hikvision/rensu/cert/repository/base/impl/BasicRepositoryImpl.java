package com.hikvision.rensu.cert.repository.base.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.hikvision.rensu.cert.repository.base.BasicRepository;

/**
 * 实现BaseRepository，原来JPARepository的方法依然可以使用
 */
public class BasicRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BasicRepository<T, ID> {

	private EntityManager entityManager;

	/*父类没有不带参数的构造方法，需要手动构造父类*/
	public BasicRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}
	
	/*暴露接口EntityManager （SimpleJpaRepository没有暴露接口）*/
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
