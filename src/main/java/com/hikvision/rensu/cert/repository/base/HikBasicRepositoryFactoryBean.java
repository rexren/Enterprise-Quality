package com.hikvision.rensu.cert.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * HikBasicRepositoryFactoryBean的写法具体参照JpaRepositoryFactoryBean
 */
public class HikBasicRepositoryFactoryBean<T extends JpaRepository<Object, Serializable>>
		extends JpaRepositoryFactoryBean<T, Object, Serializable> {

	public HikBasicRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	/**
	 * 返回createRepositoryFactory工厂实例
	 * */
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new HikBasicRepositoryFactory(em);
	}
}
