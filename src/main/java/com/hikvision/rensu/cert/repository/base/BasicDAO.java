package com.hikvision.rensu.cert.repository.base;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public interface BasicDAO {

	/**
	 * 通过SimpleJpaRepository的em属性，反射获取EntityManager的实例
	 * 
	 * @return
	 */
	default EntityManager getEntityManager() {
		try {
			Field f = SimpleJpaRepository.class.getDeclaredField("em");
			f.setAccessible(true);
			return (EntityManager) f.get(this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LoggerFactory.getLogger(BasicDAO.class)
					.error("not get current entityManager from reflect SimpleJpaRepository's field", e);
		}
		return null;
	}
}