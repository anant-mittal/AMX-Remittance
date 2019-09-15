package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.VwLoyalityEncash;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.LoyalityPointState;
import com.amx.jax.repository.VwLoyalityEncashRepository;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.JaxUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class LoyalityPointService {

	@Autowired
	VwLoyalityEncashRepository repo;
	@Autowired
	MetaData meta;
	@Autowired
	CorporateDiscountManager corporateDiscountManager;
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	JaxTenantProperties jaxTenantProperties;

	public VwLoyalityEncash getVwLoyalityEncash() {
		Iterable<VwLoyalityEncash> loyalityPointMaster = repo.findAll();
		VwLoyalityEncash view = loyalityPointMaster.iterator().next();
		return view;
	}

	public BigDecimal getTodaysLoyalityPointsEncashed() {
		return repo.getTodaysLoyalityPointsEncashed(meta.getCustomerId());
	}

	/**
	 * added by Rabil for loyality points
	 * 
	 * @param commission
	 * @return
	 */
	public BigDecimal getloyaltyAmountEncashed(BigDecimal commission) {
		BigDecimal loyalityPoints = getVwLoyalityEncash().getLoyalityPoint();
		BigDecimal loyalityPointsEncashed = getVwLoyalityEncash().getEquivalentAmount();
		BigDecimal corporateDiscount = corporateDiscountManager.corporateDiscount();
		if (JaxUtil.isNullZeroBigDecimalCheck(commission) && JaxUtil.isNullZeroBigDecimalCheck(loyalityPoints)
				&& loyalityPointsEncashed.compareTo(corporateDiscount) > 0) {
			if (commission.compareTo(loyalityPointsEncashed) >= 0) {
				loyalityPointsEncashed = loyalityPointsEncashed.subtract(BigDecimal.ZERO);
			} else {
				loyalityPointsEncashed = loyalityPointsEncashed.subtract(corporateDiscount);
			}
		}

		return loyalityPointsEncashed;
	}
	
	public LoyalityPointState getLoyalityState(BigDecimal customerId) {
		Customer customer = customerDao.getCustById(customerId);
		BigDecimal loyalityPointsAvailable = null;
		LoyalityPointState loyalityState = null;
		if(null != customer.getLoyaltyPoints()) {
			loyalityPointsAvailable = customer.getLoyaltyPoints();
		}
		BigDecimal loyaltyPointCount = jaxTenantProperties.getLoyaltyCount();
		if (loyalityPointsAvailable == null
				|| (loyalityPointsAvailable.longValue() < loyaltyPointCount.longValue())) {
			//responseModel.setLoyalityPointState(LoyalityPointState.LOYALTY_POINT_NOT_AVAILABLE);
			loyalityState = LoyalityPointState.LOYALTY_POINT_NOT_AVAILABLE;
		}
		
		return loyalityState;
	}
}
