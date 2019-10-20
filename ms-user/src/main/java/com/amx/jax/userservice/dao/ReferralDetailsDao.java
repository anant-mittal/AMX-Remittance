package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ReferralDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.ReferralDetailRepository;

@Component
public class ReferralDetailsDao {
	@Autowired
	private ReferralDetailRepository referralRepo;
	
	@Autowired
	private MetaData meta;

	public void saveReferralCode(ReferralDetails referralDetails) {
		BigDecimal customerId = referralDetails.getCustomerId();		
		referralDetails.setIsActive("Y");
		referralDetails.setCreatedBy(customerId.toString());
		referralDetails.setCreatedDate(new Date());
		referralRepo.save(referralDetails);
	}
	
	public ReferralDetails getReferralByCustomerId() {
		ReferralDetails refD = null;
		BigDecimal customerId = meta.getCustomerId();
		List<ReferralDetails> referralDetails = referralRepo.getReferalByCustomerId(customerId);
		if (referralDetails != null && !referralDetails.isEmpty()) {
			refD = referralDetails.get(0);
		}
		return refD;
	}
	
	public ReferralDetails getReferralByCustomerId(BigDecimal customerId) {
		ReferralDetails refD = null;
		List<ReferralDetails> referralDetails = referralRepo.getReferalByCustomerId(customerId);
		if (referralDetails != null && !referralDetails.isEmpty()) {
			refD = referralDetails.get(0);
		}
		return refD;
	}
	
	public ReferralDetails getReferralByCustomerReferralCode(String referralCode) {
		ReferralDetails refD = null;
		List<ReferralDetails> referralDetails = referralRepo.getReferalByCustomerReferralCode(referralCode);
		if (referralDetails != null && !referralDetails.isEmpty()) {
			refD = referralDetails.get(0);
		}
		return refD;
	}
	
	public void updateReferralCode(ReferralDetails referralDetails) {
		BigDecimal customerId = referralDetails.getCustomerId();		
		referralDetails.setIsActive("Y");
		referralDetails.setModifiedBy(customerId.toString());
		referralDetails.setModifiedDate(new Date());
		referralRepo.save(referralDetails);
	}
}
