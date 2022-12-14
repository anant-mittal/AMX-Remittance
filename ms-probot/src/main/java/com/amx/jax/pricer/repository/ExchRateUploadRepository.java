package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.pricer.dbmodel.ExchRateUpload;
import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

@Transactional
public interface ExchRateUploadRepository extends JpaRepository<ExchRateUpload, BigDecimal> {

	List<ExchRateUpload> findByRuleId(String ruleId);

	List<ExchRateUpload> findByRuleIdIn(Iterable<String> ruleIds);

	
	@Query("select u from ExchRateUpload u where u.isActive=?1 and u.status=?2 order by createdDate asc, id asc")
	List<ExchRateUpload> findByIsActiveAndStatus(IS_ACTIVE isActive, RATE_UPLOAD_STATUS status);

	@Query("select u from ExchRateUpload u where u.status=?1 order by createdDate asc, id asc")
	List<ExchRateUpload> findByStatus(RATE_UPLOAD_STATUS status);

	@Transactional
	@Modifying
	@Query("update ExchRateUpload u set u.status=?2 , u.approvedBy=?3, u.approvedDate=?4, u.isActive=?5, u.comment=?6"
			+ " where u.ruleId in ( ?1 )")
	public int updateStatusForRuleIdIn(Iterable<String> ruleIds, RATE_UPLOAD_STATUS status, String approvedBy,
			Date approvedDate, IS_ACTIVE isActive, String comment);

	@Transactional
	@Modifying
	@Query("update ExchRateUpload u set u.approvedBy=?2, u.approvedDate=?3 where u.ruleId in ( ?1 )")
	public int updateApprovalForRuleIdIn(Iterable<String> ruleIds, String approvedBy, Date approvedDate);

}
