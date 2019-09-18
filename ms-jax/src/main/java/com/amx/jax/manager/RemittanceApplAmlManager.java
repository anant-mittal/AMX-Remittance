package com.amx.jax.manager;
/**
 * @author rabil
 * 
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.service.CountryService;
import com.amx.jax.services.BankService;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceApplAmlManager {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	MetaData metaData;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private BankService bankService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	RoutingProcedureDao routingProcedureDao;
	
	public List<RemitApplAmlModel> createRemittanceApplAml(RemittanceApplication remittanceApplication,RemittanceAppBenificiary remittanceAppBeneficairy ){
		List<RemitApplAmlModel> amlList= new ArrayList<>();
		BenificiaryListView beneficiaryDT = (BenificiaryListView) remitApplParametersMap.get("BENEFICIARY");
		AmlCheckResponseDto amlDto = beneRiskAml(beneficiaryDT,remittanceAppBeneficairy);
		return amlList;
	}
	
	private AmlCheckResponseDto beneRiskAml(BenificiaryListView beneficiaryDT,RemittanceAppBenificiary remittanceAppBeneficairy ) {
		AmlCheckResponseDto amlDto = new AmlCheckResponseDto();
		BigDecimal beneCountryId = remittanceAppBeneficairy.getBeneficiaryBankCountryId();	
		CountryMaster countryMaster = countryService.getCountryMaster(beneCountryId);
		Integer riskCount = 0;
		if(countryMaster!=null) {
			riskCount = countryMaster.getBeneCountryRisk();
			if(countryMaster.getBeneCountryRisk()!=null && riskCount==1) {
				amlDto.setBlackRemark1("Bene country  Risk  Level   1 ");
			}
		}
		if(beneficiaryDT.getNationality()!=null && new BigDecimal(beneficiaryDT.getNationality()).compareTo(beneCountryId)!=0) {
			amlDto.setBlackRemark2("Remitter  Nationality  Mistmatch  with  Bene  Country");
			amlDto.setTag(ConstantDocument.Yes);
		}
		
		BigDecimal changeHistcount = routingProcedureDao.getCustomerHistroyCount(metaData.getCustomerId());
		if(changeHistcount.compareTo(BigDecimal.ZERO)>0) {
			amlDto.setBlackRemark3("Email / Mobile  changed  within  90  days");
		}
		
		if(riskCount==1 && !StringUtils.isBlank(amlDto.getTag()) && amlDto.getTag().equalsIgnoreCase(ConstantDocument.Yes) && changeHistcount.compareTo(BigDecimal.ZERO)>0) {
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
		}
		
		return amlDto;
	}
	
}
