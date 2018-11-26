package com.amx.jax.pricer.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.repository.PipsMasterRepository;

@Component
public class PipsMasterDao {

	@Autowired
	private PipsMasterRepository repo;

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(PipsMasterDao.class);

}
