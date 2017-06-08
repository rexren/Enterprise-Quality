package com.hikvision.rensu.cert.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.hikvision.rensu.cert.repository.base.impl.BasicRepositoryImpl;

/**
 * HikBasicRepositoryFactory的写法具体参照JpaRepositoryFactory
 */
public class HikBasicRepositoryFactory extends JpaRepositoryFactory {

	public HikBasicRepositoryFactory(EntityManager em) {
		super(em);
	}

	/**
	 * getRepositoryBaseClass+getTargetRepository:确定具体的实现类
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return BasicRepositoryImpl.class;
	}
	
	/**
	 * getRepositoryBaseClass+getTargetRepository:确定具体的实现类
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(
			RepositoryInformation information, EntityManager em) {
		return new BasicRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), em);
	}
}
