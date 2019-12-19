package com.amx.jax.manager;
/**
 * @author rabil
 * 
 */

import java.math.BigDecimal;
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
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CusmasModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerDetailsView;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.partner.dao.PartnerTransactionDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.BankService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxUtil;

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
	
	@Autowired
	RemittanceApplicationBeneRepository applBeneRepository;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	
	@Autowired
	ParameterService parameterService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PartnerTransactionDao partnerDetailDao;
	
	//@Autowired
	//IRemittanceTransactionRepository trnxRepository;
	
	@Autowired
	RemittanceTransactionRepository trnxRepository;
	
	
	public RemitApplAmlModel createRemittanceApplAml(RemittanceApplication remittanceApplication,RemittanceAppBenificiary remittanceAppBeneficairy){
		  RemitApplAmlModel amlModel = null;
		BenificiaryListView beneficiaryDT = (BenificiaryListView) remitApplParametersMap.get("BENEFICIARY");
		AmlCheckResponseDto amlDto = beneRiskAml(beneficiaryDT.getBeneficiaryRelationShipSeqId(),remittanceAppBeneficairy.getBeneficiaryBankCountryId(),remittanceApplication);
		
		  if(amlDto!=null &&  !StringUtils.isBlank(amlDto.getHighValueTrnxFlag()) && !StringUtils.isBlank(amlDto.getStopTrnxFlag())) { 
			  amlModel= new  RemitApplAmlModel();
			  amlModel.setCompanyId(metaData.getCompanyId());
			  amlModel.setCountryId(remittanceApplication.getFsCountryMasterByApplicationCountryId().getCountryId());
			  amlModel.setExRemittanceAppfromAml(remittanceApplication);		   
		  if(amlDto.getHighValueTrnxFlag()!=null && amlDto.getHighValueTrnxFlag().equalsIgnoreCase(ConstantDocument.Yes)) {
			  if(amlDto.getStopTrnxFlag()!=null && amlDto.getStopTrnxFlag().equalsIgnoreCase(ConstantDocument.Yes)) {
				  
			StringBuffer sb = new StringBuffer();
			sb.append(amlDto.getBlackRemark1()==null?"":amlDto.getBlackRemark1())
			.append(amlDto.getBlackRemark2()==null?"":amlDto.getBlackRemark2())
			.append(amlDto.getBlackRemark3()==null?"":amlDto.getBlackRemark3())
			.append(amlDto.getRiskLevel1()==null?"":amlDto.getRiskLevel1())
			.append(amlDto.getRiskLevel2()==null?"":amlDto.getRiskLevel2())
			.append(amlDto.getRiskLevel3()==null?"":amlDto.getRiskLevel3());
			if(sb!=null) {
				amlModel.setBlackListReason(sb.toString());
			}
			
			  }
		  }
		  	amlModel.setIsactive(ConstantDocument.Yes);
		  	amlModel.setCreatedBy(remittanceApplication.getCreatedBy());
		  	amlModel.setCreatedDate(remittanceApplication.getCreatedDate());
		  }
		 
		return amlModel;
	}
	
	public AmlCheckResponseDto beneRiskAml(BigDecimal beneReationId,BigDecimal beneficiaryBankCountryId,RemittanceApplication remittanceApplication) {
		
		BenificiaryListView beneficiaryDT =beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(metaData.getCustomerId(),beneReationId,ConstantDocument.Yes);
		AmlCheckResponseDto amlDto = new AmlCheckResponseDto();
		CountryMaster countryMaster = countryService.getCountryMaster(beneficiaryBankCountryId);
		AuthenticationLimitCheckView amlCashRisk = parameterService.getAmlRiskLevelForCash();
		BigDecimal trnxCntForRiskCntry = routingProcedureDao.getCustomerTrnxCount(beneficiaryBankCountryId,metaData.getCountryBranchId(),metaData.getCustomerId());
		CustomerDetailsView customer = partnerDetailDao.getCustomerDetails(metaData.getCustomerId());
		Customer cust = new Customer();
		cust.setCustomerId(metaData.getCustomerId());
		
		CountryBranchMdlv1 cntrybrid = new CountryBranchMdlv1();
		cntrybrid.setCountryBranchId(cntrybrid.getCountryBranchId());
		
	List<RemittanceTransaction>  trnxCountList = trnxRepository.getTotalTrnxCntForCustomer(metaData.getCustomerId(),metaData.getCountryBranchId());
		BigDecimal trnxCount = BigDecimal.ZERO;
		if(trnxCountList!=null && !trnxCountList.isEmpty()) {
			trnxCount = new BigDecimal(trnxCountList.size());
		}
		
		BigDecimal amlCashRiskLevel = BigDecimal.ZERO; 
		if(amlCashRisk!=null) {
			amlCashRiskLevel =amlCashRisk.getAuthLimit(); 
		}
		Integer riskCount = 0;
		if(countryMaster!=null) {
			riskCount = countryMaster.getBeneCountryRisk();
			if(countryMaster.getBeneCountryRisk()!=null && riskCount==1) {
				amlDto.setBlackRemark1("Bene country risk level 1.");
				amlDto.setTag(ConstantDocument.Yes);
			}
		}
		if(beneficiaryDT.getNationality()!=null && new BigDecimal(beneficiaryDT.getNationality()).compareTo(beneficiaryBankCountryId)!=0) {
			amlDto.setBlackRemark2("Remitter nationality mistmatch.");
			amlDto.setTag(ConstantDocument.Yes);
		}
		
		BigDecimal changeHistcount = routingProcedureDao.getCustomerHistroyCount(metaData.getCustomerId());
		if(JaxUtil.isNullZeroBigDecimalCheck(changeHistcount) && changeHistcount.compareTo(BigDecimal.ZERO)>0) {
			amlDto.setBlackRemark3("Email/Mobile changed within 90 days.");
		}

		
		
		
		
		logger.info("AML RISK -->riskCount "+riskCount+"\t amlCashRiskLevel :"+amlCashRiskLevel+"\t changeHistcount :"+changeHistcount+"\t Risk country value :"+riskCount+"\t trnxCntForRiskCntry :"+trnxCntForRiskCntry);
		
		if(riskCount==1 && !StringUtils.isBlank(amlDto.getTag()) 
		   && amlDto.getTag().equalsIgnoreCase(ConstantDocument.Yes) 
		   && changeHistcount.compareTo(BigDecimal.ZERO)>0) { 
			/** (a) Bene country  Risk  Level   1   (b) -New Bene   '(c)  Email/Mobile changed within 90 days **/ 
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
		}else if(riskCount==1 && changeHistcount.compareTo(BigDecimal.ZERO)>0 
				&& beneficiaryDT.getServiceGroupCode().equalsIgnoreCase(ConstantDocument.CASH)
				&& JaxUtil.isNullZeroBigDecimalCheck(amlCashRiskLevel) && remittanceApplication.getLocalTranxAmount().compareTo(amlCashRiskLevel)>1) { 
			/** Bene country  Risk  Level    1 and  Cash Trn above 200 KD **/
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
			amlDto.setRiskLevel1(amlCashRisk.getAuthMessage());
		}else if(riskCount==1 &&  JaxUtil.isNullZeroBigDecimalCheck(trnxCntForRiskCntry) && trnxCntForRiskCntry.compareTo(BigDecimal.ONE)>=1) {
			/** More than one Online transaction to the bene risk  country by a Customer on the same day **/  
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
			amlDto.setRiskLevel2("No of Online Trn = "+trnxCntForRiskCntry +" by the customer to "+countryMaster.getCountryAlpha3Code()+".");
		}else if(riskCount==1 && customer!=null && customer.getNationality().contains("PAKISTAN") && JaxUtil.isNullZeroBigDecimalCheck(trnxCount) && trnxCount.compareTo(BigDecimal.ONE)>=1 ) {
			/** ( a ) Bene country  Risk  Level   1 (b)   Remitter  Nationality  Mismatch  with  Bene  Country  and  Email / Mobil changed in last 90 days **/
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
			amlDto.setRiskLevel2("No of Online Trn = "+trnxCount +" by Pakistan nationality.");
		/** Pakistan Nationality sends more than one online transaction to any Country on the same day **/  	
		}else if(customer!=null && customer.getNationality().contains("PAKISTAN") && JaxUtil.isNullZeroBigDecimalCheck(trnxCount) && trnxCount.compareTo(BigDecimal.ONE)>=1 ) {
			amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
			amlDto.setStopTrnxFlag(ConstantDocument.Yes);
			amlDto.setRiskLevel3("No of Online Trn = "+trnxCount +" by Pakistan Nationality on the same day.");
		}else if(riskCount==1 && beneficiaryDT.getServiceGroupCode().equalsIgnoreCase(ConstantDocument.CASH)
					&& JaxUtil.isNullZeroBigDecimalCheck(amlCashRiskLevel) && remittanceApplication.getLocalTranxAmount().compareTo(amlCashRiskLevel)>0) { 
				/** Bene country  Risk  Level    1 and  Cash Trn above 200 KD **/
				amlDto.setHighValueTrnxFlag(ConstantDocument.Yes);
				amlDto.setStopTrnxFlag(ConstantDocument.Yes);
				amlDto.setRiskLevel1(amlCashRisk.getAuthMessage());
		}
		
		
		return amlDto;
	}
	
}
