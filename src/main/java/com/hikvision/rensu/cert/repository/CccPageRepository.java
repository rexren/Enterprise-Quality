package com.hikvision.rensu.cert.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CccPageRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

}
