package com.amx.jax.branchremittance.manager;
/**
 * @author rabil 
 * @date 10/29/2019
 */



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branchremittance.dao.PlaceOrderDao;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RatePlaceOrder;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.util.JaxUtil;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class PlaceOrderManager implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BranchRemittanceApplManager brApplManager;
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	IDocumentDao documentDao;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	BankMetaService bankMetaService;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PlaceOrderDao placeOrderDao;
	
	@Autowired
	CorporateDiscountManager corporateDiscountManager;
	

	
	
	public Boolean savePlaceOrder(PlaceOrderRequestModel placeOrderRequestModel) {
		 Boolean boolRespModel = false;
		 try {
		 RatePlaceOrder placeOrderAppl  =createPlaceOrder(placeOrderRequestModel);
		 HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		 mapAllDetailApplSave.put("EX_RATE_PLACE_ORD", placeOrderAppl);
		 placeOrderDao.savePlaceOrder(mapAllDetailApplSave);
		 boolRespModel =true;
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return boolRespModel;
	}
	
	
	
	public RatePlaceOrder createPlaceOrder(PlaceOrderRequestModel placeOrderRequestModel) {
		
		
		
		RatePlaceOrder placeOrderAppl = new RatePlaceOrder();
		
		if(placeOrderRequestModel!=null && placeOrderRequestModel.getApplRequestModel() !=null) {
		BranchRemittanceApplRequestModel applRequestModel =placeOrderRequestModel.getApplRequestModel();
		BenificiaryListView beneficaryDetails =brApplManager.getBeneDetails(applRequestModel);
		if(applRequestModel.getDynamicRroutingPricingBreakup()!=null) {
		DynamicRoutingPricingDto dynPricingDto = applRequestModel.getDynamicRroutingPricingBreakup();
		TrnxRoutingDetails trnxRoutingDtls = dynPricingDto.getTrnxRoutingPaths();
		BigDecimal selectedCurrId = branchRemitManager.getSelectedCurrency(beneficaryDetails.getCurrencyId(), applRequestModel);
		Customer customer = customerRepository.getCustomerByCustomerId(metaData.getCustomerId());
		
		//place order
		placeOrderAppl.setAccountSeqquenceId(beneficaryDetails.getBeneficiaryAccountSeqId());
		placeOrderAppl.setApplDocumentFinanceYear(null);
		placeOrderAppl.setApplDocumentNumber(null);
		placeOrderAppl.setApplicationCountryId(metaData.getCountryId());
		placeOrderAppl.setAreaName(null);
		placeOrderAppl.setBeneficiaryAccountNo(beneficaryDetails.getBankAccountNumber());
		placeOrderAppl.setBeneficiaryBankId(beneficaryDetails.getBankId());
		placeOrderAppl.setBeneficiaryCountryId(beneficaryDetails.getBenificaryCountry());
		placeOrderAppl.setBeneficiaryMasterId(beneficaryDetails.getBeneficaryMasterSeqId());
		placeOrderAppl.setBeneficiaryRelationId(beneficaryDetails.getBeneficiaryRelationShipSeqId());
		
		placeOrderAppl.setCompanyId(metaData.getCompanyId());
		setCustomerDiscountColumns(placeOrderAppl,dynPricingDto);
		
		placeOrderAppl.setDeliveryModeId(trnxRoutingDtls.getDeliveryModeId());
		placeOrderAppl.setDestinationCurrenyId(beneficaryDetails.getCurrencyId());
		
		// fin year
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		if(userFinancialYear!=null) {
			placeOrderAppl.setDocumentFinanceYear(userFinancialYear.getFinancialYear());
		}else {
			throw new GlobalException(JaxError.NULL_FINANCIAL_YEAR,"Financial error is not defined");
		}
		
		
		placeOrderAppl.setCountryBranchId(metaData.getCountryBranchId());
		placeOrderAppl.setCreatedBy(brApplManager.getEmployeeDetails().getUserName());
		placeOrderAppl.setCreatedDate(new Date());
		placeOrderAppl.setCustomerId(metaData.getCustomerId());
		placeOrderAppl.setCustomerreference(customer.getCustomerReference());
		placeOrderAppl.setCustomerEmail(customer.getEmail());
		placeOrderAppl.setIsActive(ConstantDocument.Status.U.toString());
		placeOrderAppl.setReachedCostRateLimit(dynPricingDto.getCostRateLimitReached()==false?"N":"Y");
		placeOrderAppl.setRemarks(placeOrderRequestModel.getRemarks());
		placeOrderAppl.setRemitDocumentYear(null);
		placeOrderAppl.setRoutingBankId(trnxRoutingDtls.getRoutingBankId());
		placeOrderAppl.setRequestCurrencyId(selectedCurrId);
		placeOrderAppl.setRoutingBranchId(applRequestModel.getRoutingBankBranchId());
		placeOrderAppl.setRoutingCountryId(trnxRoutingDtls.getRoutingCountryId());	
		placeOrderAppl.setTransactionAmountPaid(dynPricingDto.getExRateBreakup().getConvertedLCAmount());
		placeOrderAppl.setRemitType(beneficaryDetails.getServiceGroupId());
		placeOrderAppl.setServiceMasterId(applRequestModel.getServiceMasterId());
		placeOrderAppl.setRemittanceModeId(trnxRoutingDtls.getRemittanceModeId());
		placeOrderAppl.setDeliveryModeId(trnxRoutingDtls.getDeliveryModeId());
		placeOrderAppl.setSourceOfincomeId(applRequestModel.getSourceOfFund());
		
		if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getForeignAmount())){
			placeOrderAppl.setTransactionAmount(applRequestModel.getForeignAmount());
		}else {
			placeOrderAppl.setTransactionAmount(applRequestModel.getLocalAmount());
		}
		
		
		if(!StringUtils.isBlank(dynPricingDto.getDiscountOnComissionFlag()) 
				&& dynPricingDto.getDiscountOnComissionFlag().equalsIgnoreCase(ConstantDocument.Yes)) {
			placeOrderAppl.setDiscountOnCommission(corporateDiscountManager.corporateDiscount());
		}
		
		
		
		
		Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_PLACEORDER).get(0);
		if(document==null) {
			throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO,"Place order document code is not defined in the document master");
		}
		
		placeOrderAppl.setDocumentCode(document.getDocumentCode());
		placeOrderAppl.setDocumentId(document.getDocumentID());
		
		
		CountryBranchMdlv1 countryBranch = bankMetaService.getCountryBranchById(metaData.getCountryBranchId()); //user branch not customer branch
		
		
		BigDecimal documentNo = branchRemitManager.generateDocumentNumber(metaData.getCountryId(), metaData.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_PLACEORDER, userFinancialYear.getFinancialYear(), ConstantDocument.Status.U.toString(), countryBranch.getBranchId());
		if(JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
			placeOrderAppl.setDocumentNumber(documentNo);
		}else {
			throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO,"Application document number shouldnot be null or blank");
		}		
		
		}else {
			throw new GlobalException(JaxError.INVALID_INPUT,"Dynamic routing and pricing details.");
		}
		}else {
			throw new GlobalException(JaxError.INVALID_INPUT,"Invalid request model");
		}
		
		return placeOrderAppl;
	}

	
	
	public void setCustomerDiscountColumns(RatePlaceOrder remittanceApplication,RemittanceTransactionResponsetModel remittanceTransactionResponsetModel) {
		remittanceApplication.setIsDiscountAvailed(Boolean.TRUE.equals(remittanceTransactionResponsetModel.getDiscountAvailed()) ? ConstantDocument.Yes: ConstantDocument.No);
		Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscoutDetails = remittanceTransactionResponsetModel.getCustomerDiscountDetails();
		if (customerDiscoutDetails != null && !customerDiscoutDetails.isEmpty()) {
			remittanceApplication.setCusCatDiscountId(customerDiscoutDetails.get(DISCOUNT_TYPE.CUSTOMER_CATEGORY).getId());
			remittanceApplication.setCusCatDiscount(customerDiscoutDetails.get(DISCOUNT_TYPE.CUSTOMER_CATEGORY).getDiscountPipsValue());
			remittanceApplication.setChannelDiscountId(customerDiscoutDetails.get(DISCOUNT_TYPE.CHANNEL).getId());
			remittanceApplication.setChannelDiscount(customerDiscoutDetails.get(DISCOUNT_TYPE.CHANNEL).getDiscountPipsValue());
			String pips = customerDiscoutDetails.get(DISCOUNT_TYPE.AMOUNT_SLAB).getDiscountTypeValue();
			if (!StringUtils.isBlank(pips)) {
				String[] parts = pips.split("-");
				remittanceApplication.setPipsFromAmt(parts[0] == null ? new BigDecimal(0) : new BigDecimal(parts[0]));
				remittanceApplication.setPipsToAmt(parts[1] == null ? new BigDecimal(0) : new BigDecimal(parts[1]));
			}
			remittanceApplication.setPipsDiscount(customerDiscoutDetails.get(DISCOUNT_TYPE.AMOUNT_SLAB).getDiscountPipsValue());
		}
	}
	
}
