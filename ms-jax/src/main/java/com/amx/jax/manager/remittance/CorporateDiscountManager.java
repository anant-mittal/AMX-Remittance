package com.amx.jax.manager.remittance;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCoreDetailsView;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.CorporateDiscountDto;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.CustomerCoreDetailsRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CorporateDiscountManager {

	@Autowired
	private MetaData metaData;
	@Autowired
	CustomerCoreDetailsRepository customerCoreDetailsRepositroy;	
	@Autowired
	CountryBranchRepository countryBranchRepository;
	@Autowired
	ICurrencyDao currencyDao;


	/** Added by Rabil for corporate employee discount **/
	public CorporateDiscountDto corporateDiscount(BigDecimal commission) {
		
		CorporateDiscountDto corpDiscntDto = new CorporateDiscountDto();
		
		BigDecimal corpDiscount = BigDecimal.ZERO;
		BigDecimal corpDiscountId =null;
		BigDecimal corpDiscountIdForOnline =null;
		Customer customer = new Customer();
		customer.setCustomerId(metaData.getCustomerId());
        CountryBranchMdlv1 countryBranch = null;
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		if(JaxUtil.isNullZeroBigDecimalCheck(countryBranchId)) {
			countryBranch = countryBranchRepository.findByCountryBranchId(countryBranchId);
		}
		CustomerCoreDetailsView customercoreView = customerCoreDetailsRepositroy.findByCustomerID(metaData.getCustomerId());
		
		if(customercoreView != null && countryBranch!=null && countryBranch.getBranchId().compareTo(ConstantDocument.ONLINE_BRANCH_LOC_CODE)==0) {
			corpDiscount = customercoreView.getCorporateoDiscountAmountforOnline() == null ? BigDecimal.ZERO: customercoreView.getCorporateoDiscountAmountforOnline();
			corpDiscountId = customercoreView.getCorporateIdForOnline();
			
			BigDecimal percentageAmt = BigDecimal.ZERO;
			BigDecimal corDisPercentage = customercoreView.getDiscountOnComPercentage();
			BigDecimal corPerId     = customercoreView.getDiscountOnComPercentageId();
			if(JaxUtil.isNullZeroBigDecimalCheck(corDisPercentage) && JaxUtil.isNullZeroBigDecimalCheck(corPerId)){
				percentageAmt = getPercentageAmount(corDisPercentage,commission);
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(percentageAmt) && percentageAmt.compareTo(corpDiscount)==1) {
				corpDiscount = percentageAmt;
				corpDiscountId = corPerId;
			}
		}else { //For Branch
				corpDiscount = customercoreView.getCorporateDiscountAmount() == null ? BigDecimal.ZERO: customercoreView.getCorporateDiscountAmount();
				corpDiscountId = customercoreView.getCorporateIdForBranch();	
		}		
		
		
		
		
		corpDiscntDto.setCorpDiscount(corpDiscount);
		corpDiscntDto.setCorpDiscountId(corpDiscountId);
		
		
		return corpDiscntDto;
	}

	private BigDecimal getPercentageAmount(BigDecimal percentage,BigDecimal commssion) {
		BigDecimal percentageAmt = BigDecimal.ZERO;
		try {
		BigDecimal percentageValue = percentage.divide(new BigDecimal(100));
		BigDecimal percentageAmount = percentageValue.multiply(commssion);
		CurrencyMasterMdlv1 currencyMaster =currencyDao.findOne(metaData.getDefaultCurrencyId());
		BigDecimal decimalValue = currencyMaster.getDecinalNumber()==null?BigDecimal.ZERO:currencyMaster.getDecinalNumber();
		percentageAmt = RoundUtil.roundBigDecimal(percentageAmount,decimalValue.intValue());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return percentageAmt;
	}
	
	public static void main(String[] args) {
		CorporateDiscountManager corp = new CorporateDiscountManager();
		BigDecimal kdAmt = new BigDecimal("1");
		CorporateDiscountDto dto = corp.corporateDiscount(kdAmt);
		System.out.println("test :"+dto.getCorpDiscount());
	}

}
