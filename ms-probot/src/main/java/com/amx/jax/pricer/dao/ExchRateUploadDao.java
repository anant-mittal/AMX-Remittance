package com.amx.jax.pricer.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
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

	public List<ExchRateUpload> getByRuleIdIn(Iterable<String> ruleIds) {
		return repo.findByRuleIdIn(ruleIds);
	}

	public List<ExchRateUpload> getRulesByStatusAndActive(RATE_UPLOAD_STATUS status, IS_ACTIVE active) {
		return repo.findByIsActiveAndStatus(active, status);
	}

	public List<ExchRateUpload> getRulesByStatus(RATE_UPLOAD_STATUS status) {
		return repo.findByStatus(status);
	}

	@Transactional
	@Modifying
	public List<ExchRateUpload> saveAll(List<ExchRateUpload> exchRateUploads) throws DataIntegrityViolationException {
		return (List<ExchRateUpload>) repo.save(exchRateUploads);
	}

	@Transactional
	@Modifying
	public int updateStatusForRuleIdIn(Iterable<String> ruleIds, RATE_UPLOAD_STATUS status, String approvedBy,
			Date approvedDate) {
		return repo.updateStatusForRuleIdIn(ruleIds, status, approvedBy, approvedDate);
	}

}
