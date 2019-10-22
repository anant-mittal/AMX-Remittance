package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.JobProgressModel;

public interface JobProgressRepository extends CrudRepository<JobProgressModel, Serializable> {
	
}
