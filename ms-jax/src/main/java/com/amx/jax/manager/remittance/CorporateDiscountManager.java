package com.amx.jax.manager.remittance;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCoreDetailsView;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CustomerCoreDetailsRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CorporateDiscountManager {

	@Autowired
	private MetaData metaData;
	@Autowired
	CustomerCoreDetailsRepository customerCoreDetailsRepositroy;

	/** Added by Rabil for corporate employee discount **/
	public BigDecimal corporateDiscount() {
		BigDecimal corpDiscount = BigDecimal.ZERO;
		Customer customer = new Customer();
		customer.setCustomerId(metaData.getCustomerId());

		CustomerCoreDetailsView customercoreView = customerCoreDetailsRepositroy
				.findByCustomerID(metaData.getCustomerId());
		if (customercoreView != null) {
			corpDiscount = customercoreView.getCorporateDiscountAmount() == null ? BigDecimal.ZERO
					: customercoreView.getCorporateDiscountAmount();
		}
		return corpDiscount;
	}
}
