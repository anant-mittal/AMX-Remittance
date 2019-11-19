package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExGlJobStatus;
import com.amx.jax.pricer.var.PricerServiceConstants.GL_JOB_STATUS;

public interface VwExGlJobStatusRepo extends CrudRepository<VwExGlJobStatus, Serializable> {

	List<VwExGlJobStatus> findByJobStartDateLessThan(Date startDate);

	List<VwExGlJobStatus> findByJobStatus(GL_JOB_STATUS status);
}
