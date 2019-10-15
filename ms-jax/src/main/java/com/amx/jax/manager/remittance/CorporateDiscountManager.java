package com.amx.jax.manager.remittance;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCoreDetailsView;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.CustomerCoreDetailsRepository;
import com.amx.jax.util.JaxUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CorporateDiscountManager {

	@Autowired
	private MetaData metaData;
	@Autowired
	CustomerCoreDetailsRepository customerCoreDetailsRepositroy;	
	@Autowired
	CountryBranchRepository countryBranchRepository;


	/** Added by Rabil for corporate employee discount **/
	public BigDecimal corporateDiscount() {
		BigDecimal corpDiscount = BigDecimal.ZERO;
		Customer customer = new Customer();
		customer.setCustomerId(metaData.getCustomerId());
        CountryBranch countryBranch = null;
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		if(JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			countryBranch = countryBranchRepository.findByCountryBranchId(countryBranchId);
		}
		
		CustomerCoreDetailsView customercoreView = customerCoreDetailsRepositroy.findByCustomerID(metaData.getCustomerId());
		
		if(customercoreView != null && countryBranch!=null && countryBranch.getBranchId().compareTo(ConstantDocument.ONLINE_BRANCH_LOC_CODE)==0) {
			corpDiscount = customercoreView.getCorporateoDiscountAmountforOnline() == null ? BigDecimal.ZERO: customercoreView.getCorporateoDiscountAmountforOnline();
		}else {
				corpDiscount = customercoreView.getCorporateDiscountAmount() == null ? BigDecimal.ZERO: customercoreView.getCorporateDiscountAmount();
		}
		return corpDiscount;
	}
}
