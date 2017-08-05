package com.hikvision.ga.hephaestus.product.support;

import com.hikvision.ga.hephaestus.product.Component;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rensu on 2017/7/20.
 */
public interface ComponentRepository extends JpaRepository<Component, Long> {
}
