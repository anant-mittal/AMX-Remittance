package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.VwLoyalityEncash;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.VwLoyalityEncashRepository;
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
}
