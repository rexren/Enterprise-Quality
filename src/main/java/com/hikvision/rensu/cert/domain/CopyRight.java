package com.hikvision.rensu.cert.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CopyRight implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5436058156917876411L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//TODO add more items
	
}