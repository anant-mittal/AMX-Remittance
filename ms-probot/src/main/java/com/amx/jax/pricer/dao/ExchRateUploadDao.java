package com.amx.jax.pricer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ExchRateUpload;
import com.amx.jax.pricer.repository.ExchRateUploadRepository;
import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

@Component
public class ExchRateUploadDao {

	@Autowired
	ExchRateUploadRepository repo;

	public List<ExchRateUpload> getByRuleId(String ruleId) {
		return repo.findByRuleId(ruleId);
	}

	public List<ExchRateUpload> getActiveRulesByStatus(RATE_UPLOAD_STATUS status) {
		return repo.findByIsActiveAndStatus(IS_ACTIVE.Y, status);
	}

	public List<ExchRateUpload> saveAll(List<ExchRateUpload> exchRateUploads) {
		return (List<ExchRateUpload>) repo.save(exchRateUploads);
	}

}
