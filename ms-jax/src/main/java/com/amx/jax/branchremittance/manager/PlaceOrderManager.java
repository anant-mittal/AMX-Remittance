
package com.amx.jax.branchremittance.manager;
/**
 * @author rabil 
 * @date 10/29/2019
 */



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.dao.PlaceOrderDao;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.CurrencyOtherInformation;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RatePlaceOrder;
import com.amx.jax.dbmodel.remittance.ViewPlaceOnOrderInquiry;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.service.ExchangeRateService;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderUpdateStatusDto;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.CountryWiseCountOrderDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.GsmPlaceOrderListDto;
import com.amx.jax.model.response.remittance.GsmSearchRequestParameter;
import com.amx.jax.model.response.remittance.PlaceOrderApplDto;
import com.amx.jax.model.response.remittance.RatePlaceOrderInquiryDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICurrencyOtherInfoRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.IRatePlaceOrderRepository;
import com.amx.jax.repository.remittance.IViewPlaceOnOrderInquiryRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	
	@Autowired
	IRatePlaceOrderRepository ratePlaceOrderRepository;
	
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	
	@Autowired
	ICurrencyDao currDao;
	
	@Autowired
	ExchangeRateService exchangeRateService;
	
	@Autowired
	RemittanceTransactionManager remittanceTransactionManager;
	
	@Resource
	private Map<String, Object> remitApplParametersMap;
	
	@Autowired
	IViewPlaceOnOrderInquiryRepository viewPlaceOnOrderInquiryRepository; 
	
	@Autowired
	ICurrencyOtherInfoRepository currencyOtherInfoRepository;
	
	
	@Autowired
	ParameterService parameterService;
	
	
	
	public Boolean savePlaceOrder(PlaceOrderRequestModel placeOrderRequestModel) {
		 Boolean boolRespModel = false;
		 try {
		 RatePlaceOrder placeOrderAppl  =createPlaceOrder(placeOrderRequestModel);
		 HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		 mapAllDetailApplSave.put("EX_RATE_PLACE_ORD", placeOrderAppl);
		 placeOrderDao.savePlaceOrder(mapAllDetailApplSave);
		 boolRespModel =true;
		}catch(GlobalException e){
				logger.debug("create application", e.getErrorMessage() + "" +e.getErrorKey());
				throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
			}
		 return boolRespModel;
	}
	
	
	
	public RatePlaceOrder createPlaceOrder(PlaceOrderRequestModel placeOrderRequestModel) {
		
		
		
		RatePlaceOrder placeOrderAppl = new RatePlaceOrder();
		
		if(placeOrderRequestModel!=null && placeOrderRequestModel.getApplRequestModel() !=null) {
		BranchRemittanceApplRequestModel applRequestModel =placeOrderRequestModel.getApplRequestModel();
		BenificiaryListView beneficaryDetails =brApplManager.getBeneDetails(applRequestModel);
		if(applRequestModel.getDynamicRroutingPricingBreakup()!=null) {
			
			validatePlaceOrderRequest(applRequestModel,beneficaryDetails);
			
			
		DynamicRoutingPricingDto dynPricingDto = applRequestModel.getDynamicRroutingPricingBreakup();
		TrnxRoutingDetails trnxRoutingDtls = dynPricingDto.getTrnxRoutingPaths();
		BigDecimal selectedCurrId = branchRemitManager.getSelectedCurrency(beneficaryDetails.getCurrencyId(), applRequestModel);
		Customer customer = customerRepository.getCustomerByCustomerId(metaData.getCustomerId());
		ExchangeRateBreakup exchRateBreakup = applRequestModel.getExchangeRateBreakup();
		
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
		
		placeOrderAppl.setExchangeRateApplied(exchRateBreakup.getInverseRate());
		placeOrderAppl.setRackExchangeRate(dynPricingDto.getRackExchangeRate());
		placeOrderAppl.setValueDate(new Date());
		placeOrderAppl.setRequestModel(JsonUtil.toJson(applRequestModel));
		
		if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getLocalAmount())){
			placeOrderAppl.setTransactionAmount(applRequestModel.getLocalAmount());
		}else {
			placeOrderAppl.setTransactionAmount(applRequestModel.getForeignAmount());
			
		}
		
		
		if(!StringUtils.isBlank(dynPricingDto.getDiscountOnComissionFlag()) 
				&& dynPricingDto.getDiscountOnComissionFlag().equalsIgnoreCase(ConstantDocument.Yes)) {
			placeOrderAppl.setDiscountOnCommission(corporateDiscountManager.corporateDiscount());
		}
		
		placeOrderAppl.setTerminalId(metaData.getDeviceIp());
		
		
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
	
	/** 
	 * @author rabil
	 * @return :to return list of place Order for customer.
	 */
	public List<PlaceOrderApplDto> getPlaceOrderList(){
		List<PlaceOrderApplDto> list = new ArrayList<>();
		List<RatePlaceOrder> placeOrderLsit = ratePlaceOrderRepository.fetchPlaceOrderForCustomer(metaData.getCustomerId());
		if(placeOrderLsit!=null && !placeOrderLsit.isEmpty()) {
				list = convertGsmDto(placeOrderLsit);
				
			}
		return list;
	}

	
	public List<RatePlaceOrderInquiryDto> getPlaceOrderInquiry(BigDecimal countryBranchId) {
		List<RatePlaceOrderInquiryDto> poInqList = new ArrayList<RatePlaceOrderInquiryDto>();
		List <ViewPlaceOnOrderInquiry> viewPOderInqList = viewPlaceOnOrderInquiryRepository.findByCountryBranchId(countryBranchId==null?metaData.getCountryBranchId():countryBranchId);
		if(viewPOderInqList!=null && !viewPOderInqList.isEmpty()) {
			for(ViewPlaceOnOrderInquiry ratePlaceOrder :viewPOderInqList) {
				
				RatePlaceOrderInquiryDto lstPlaceOrder = new RatePlaceOrderInquiryDto();
				
				lstPlaceOrder.setBeneficiaryAccountNumber(ratePlaceOrder.getBeneficiaryAccountNumber());
				lstPlaceOrder.setBeneficiaryBankBranchName(ratePlaceOrder.getBeneficiaryBankBranchName());
				lstPlaceOrder.setBeneficiaryBankName(ratePlaceOrder.getBeneficiaryBankName());
				lstPlaceOrder.setBeneficiaryFullName(ratePlaceOrder.getBeneficiaryFullName());
				lstPlaceOrder.setCountryBranchId(ratePlaceOrder.getCountryBranchId());
				lstPlaceOrder.setCreatedBy(ratePlaceOrder.getCreatedBy());
				lstPlaceOrder.setCurrencyQuote(ratePlaceOrder.getCurrencyQuote());
				lstPlaceOrder.setCustomerIdType(ratePlaceOrder.getCustomerIdType());
				lstPlaceOrder.setCustomerNumber(ratePlaceOrder.getCustomerNumber());
				lstPlaceOrder.setIdNo(ratePlaceOrder.getIdNo());
				lstPlaceOrder.setCustomerFullName(ratePlaceOrder.getCustomerFullName());
				lstPlaceOrder.setTransactionAmount(ratePlaceOrder.getTransactionAmount());
				lstPlaceOrder.setRateOffered(ratePlaceOrder.getRateOffered());
				lstPlaceOrder.setNegotiate(ratePlaceOrder.getNegotiate());
				lstPlaceOrder.setIsActive(ratePlaceOrder.getIsActive());

				if(ratePlaceOrder.getNegotiate() != null && ratePlaceOrder.getNegotiate().equalsIgnoreCase(ConstantDocument.Yes)){
					if(ratePlaceOrder.getIsActive() != null && ratePlaceOrder.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)){
						lstPlaceOrder.setStatus(ConstantDocument.Statusd.APPROVED.toString());
					}else{
						lstPlaceOrder.setStatus(ConstantDocument.Statusd.NEGOTIATED.toString());
					}
				}else{
					if(ratePlaceOrder.getIsActive() != null && ratePlaceOrder.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)){
						lstPlaceOrder.setStatus(ConstantDocument.Statusd.APPROVED.toString());
					}else{
						lstPlaceOrder.setStatus(ConstantDocument.Statusd.UNAPPROVED.toString());
					}
				}

				poInqList.add(lstPlaceOrder);
			}
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Record not found");
		}
		
		return poInqList;
	}
	
	
	
	public Boolean updateRatePlaceOrder(PlaceOrderUpdateStatusDto dto){
		 Boolean boolRespModel = false;
		try {
			List<BigDecimal> placeOrderIdList = dto.getRatePlaceOrderIdList();
			if(placeOrderIdList!=null && !placeOrderIdList.isEmpty()) {
				
		  for(BigDecimal ratePlaceOrderId : placeOrderIdList) {
			String flag = dto.getFlag();
			String remarks = dto.getRemarks();
		if(JaxUtil.isNullZeroBigDecimalCheck(ratePlaceOrderId) 
			&& !StringUtils.isBlank(flag) ) {
			//&& (flag.equalsIgnoreCase(ConstantDocument.Status.N.toString()) || flag.equalsIgnoreCase(ConstantDocument.Status.R.toString()))) {
			RatePlaceOrder ratePlaceOrder = ratePlaceOrderRepository.findOne(ratePlaceOrderId);
			if(ratePlaceOrder!=null) {
			
			if(!StringUtils.isBlank(ratePlaceOrder.getIsActive()) 
				&& !ratePlaceOrder.getIsActive().equalsIgnoreCase(ConstantDocument.Status.Y.toString()) 
				&& !JaxUtil.isNullZeroBigDecimalCheck(ratePlaceOrder.getApplDocumentNumber()) && !JaxUtil.isNullZeroBigDecimalCheck(ratePlaceOrder.getApplDocumentFinanceYear())) {	
			
			if(flag.equalsIgnoreCase(ConstantDocument.Status.N.toString())){  /** Negotiate Place Order **/
			ratePlaceOrder.setNegotiateSts(ConstantDocument.Status.N.toString());
			ratePlaceOrder.setIsActive(ConstantDocument.Status.U.toString());
			}else if(flag.equalsIgnoreCase(ConstantDocument.Status.R.toString())){ /** Reject Place Order **/
				ratePlaceOrder.setIsActive(ConstantDocument.Status.D.toString());
			}else if(flag.equalsIgnoreCase(ConstantDocument.Status.A.toString())){ /** Accept place Order **/
				ratePlaceOrder.setIsActive(ConstantDocument.Status.Y.toString());
				/** Offerd  place Order  rate by GSM**/
			}else if(flag.equalsIgnoreCase(ConstantDocument.Status.O.toString()) && JaxUtil.isNullZeroBigDecimalCheck(dto.getExchangeRateOffered())) { 
				ratePlaceOrder.setIsActive(ConstantDocument.Status.Y.toString());
				ratePlaceOrder.setRateOffered(dto.getExchangeRateOffered());
				ratePlaceOrder.setAppointmentTime(new Date());
				ExchangeRateBreakup exchRate = getExchangeRateBreakUPForPlaceOrder(ratePlaceOrder);
				if(exchRate!=null) {
					
					if(JaxUtil.isNullZeroBigDecimalCheck(ratePlaceOrder.getRequestCurrencyId()) && ratePlaceOrder.getRequestCurrencyId().compareTo(metaData.getDefaultCurrencyId())==0) {
						ratePlaceOrder.setTransactionAmount(exchRate.getConvertedLCAmount());
					}else {
						ratePlaceOrder.setTransactionAmount(exchRate.getConvertedFCAmount());
					}
				ratePlaceOrder.setTransactionAmountPaid(exchRate.getConvertedLCAmount());
					
				}
			}
			ratePlaceOrder.setModifiedBy(brApplManager.getEmployeeDetails().getUserName());
			ratePlaceOrder.setModifiedDate(new Date());
			ratePlaceOrder.setCustomerIndicator(ConstantDocument.Status.C.toString());
			ratePlaceOrder.setApprovedDate(new Date());
			ratePlaceOrder.setApprovedBy(brApplManager.getEmployeeDetails().getUserName());
			if(!StringUtils.isBlank(remarks)) {
				ratePlaceOrder.setRemarks(remarks);
			}
			ratePlaceOrderRepository.save(ratePlaceOrder);
			boolRespModel=true;
			}else {
				throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Application is already created for this order");
			}
			}else {
				throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Record not found for the customerId :"+metaData.getCustomerId()+" and Place order id :"+ratePlaceOrderId);
			}
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Record not found for the customerId :"+metaData.getCustomerId()+" and Place order id :"+ratePlaceOrderId);
		}
		
		}
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"No record found");
		}
		}catch(GlobalException e){
			logger.debug("create application", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		return boolRespModel;
		
	}
	
	
public GsmPlaceOrderListDto getCountryWisePlaceOrderCount(GsmSearchRequestParameter requestParameter) {
	GsmPlaceOrderListDto gsmCountryWiseDto = new GsmPlaceOrderListDto();
	try {
		 List<Object[]> countryWiseCount = ratePlaceOrderRepository.getPlaceOrderCountryWiseCoount();
		 List<CountryWiseCountOrderDto> countryWiseCountList=new ArrayList<CountryWiseCountOrderDto>();
		 List<RatePlaceOrder> fetchPlaceOrder = null;
		 List<PlaceOrderApplDto> dtoGsm = null;
		
		if(countryWiseCount!=null  &&  !countryWiseCount.isEmpty()) {
			for(Object countrywiseCount : countryWiseCount) {
				Object[] count = (Object[])countrywiseCount;
				CountryWiseCountOrderDto dto = new CountryWiseCountOrderDto();
				if(count.length>=3) {
					dto.setCountryId(new BigDecimal(count[0].toString()));
					dto.setCountryName(count[1].toString());
					dto.setTotalCount(new BigDecimal(count[2].toString()));
				}
				countryWiseCountList.add(dto);
			}
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Record not found");
		}
		gsmCountryWiseDto.setCountryWiseCountList(countryWiseCountList);
		
		
		
		if(requestParameter!=null && JaxUtil.isNullZeroBigDecimalCheck(requestParameter.getBeneCountryId())) {
		 fetchPlaceOrder = ratePlaceOrderRepository.fetchByBeneficiaryCountryId(requestParameter.getBeneCountryId());
		}
		
		if(fetchPlaceOrder!=null && !fetchPlaceOrder.isEmpty()) {
		 dtoGsm= convertGsmDto(fetchPlaceOrder);
		}
		if(dtoGsm!=null && !dtoGsm.isEmpty()) {
			gsmCountryWiseDto.setGsmPlaceOrderList(dtoGsm);
		}
				
		}catch(GlobalException e){
			logger.debug("create application", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
	return gsmCountryWiseDto;
}
	

public List<PlaceOrderApplDto>  convertGsmDto(List<RatePlaceOrder> placeOrderLsit){
	List<PlaceOrderApplDto> list = new ArrayList<PlaceOrderApplDto>();
	 ObjectMapper mapper = new ObjectMapper();
	 try {
	
	for(RatePlaceOrder placeOrder : placeOrderLsit) {
		PlaceOrderApplDto applDto = new PlaceOrderApplDto();
		
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(placeOrder.getCustomerId(),placeOrder.getBeneficiaryRelationId(),ConstantDocument.Yes);
		Customer customer = customerRepository.getCustomerByCustomerId(placeOrder.getCustomerId());
		CurrencyMasterMdlv1 localCurr = currDao.getOne(metaData.getDefaultCurrencyId());
		ExchangeRateBreakup exRateBreakUp = null;
		String requestJson = placeOrder.getRequestModel();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BranchRemittanceApplRequestModel requestModelObject = mapper.readValue(requestJson, BranchRemittanceApplRequestModel.class);
		
		applDto.setCustomerId(placeOrder.getCustomerId());
		applDto.setPlaceOrderId(placeOrder.getRatePlaceOrderId());
		applDto.setBeneficiaryAccountNo(beneficaryDetails==null?"":beneficaryDetails.getBankAccountNumber());
		applDto.setBeneficiaryName(beneficaryDetails.getBenificaryName());
		applDto.setRemarks(placeOrder.getRemarks());
		applDto.setCustomerReference(customer.getCustomerReference());
		applDto.setCustomerName(getCustomerFullName(customer));
		applDto.setExchangeRateOfferd(placeOrder.getRateOffered());
		applDto.setDocumentId(placeOrder.getDocumentId());
		applDto.setDocumentFinanceYear(placeOrder.getDocumentNumber());
		applDto.setDocumentFinanceYear(placeOrder.getDocumentFinanceYear());
		applDto.setBeneficiaryBank(beneficaryDetails.getBankName());
		applDto.setBeneficiaryBranch(beneficaryDetails.getBankBranchName());
		applDto.setCompanyId(placeOrder.getCompanyId());
		applDto.setCreatedBy(placeOrder.getCreatedBy());
		applDto.setApprovedBy(placeOrder.getApprovedBy());
		applDto.setLocalcurrency(metaData.getDefaultCurrencyId());
		applDto.setForeigncurrency(placeOrder.getDestinationCurrenyId());
		applDto.setForeigncurrencyName(beneficaryDetails.getCurrencyQuoteName());
		applDto.setLocalcurrencyName(localCurr==null?"":localCurr.getQuoteName());
		applDto.setCustomerEmailId(placeOrder.getCustomerEmail());
		applDto.setSpecialOrCommonPoolIndicator(placeOrder.getCustomerIndicator());
		applDto.setApprovedBy(placeOrder.getApprovedBy());
		
		
		exRateBreakUp = getExchangeRateBreakUPForPlaceOrder(placeOrder);
		exRateBreakUp.setInverseRate(placeOrder.getRateOffered());
		
				
		 if(JaxUtil.isNullZeroBigDecimalCheck(placeOrder.getRequestCurrencyId()) && placeOrder.getRequestCurrencyId().compareTo(metaData.getDefaultCurrencyId())==0) { 
			 applDto.setLocalTranxAmount(placeOrder.getTransactionAmount());
		   // exRateBreakUp =exchangeRateService.createBreakUp(placeOrder.getRateOffered()==null?placeOrder.getExchangeRateApplied():placeOrder.getRateOffered(),placeOrder.getTransactionAmount()); 
			  if(exRateBreakUp!=null) {
				  applDto.setForeignTranxAmount(exRateBreakUp.getConvertedFCAmount()); 
			  } 
		  }else
		  { applDto.setForeignTranxAmount(placeOrder.getTransactionAmount());
		      //exRateBreakUp =exchangeRateService.createBreakUpFromForeignCurrency(placeOrder. getRateOffered()==null?placeOrder.getExchangeRateApplied():placeOrder.getRateOffered(), placeOrder.getTransactionAmount()); 
		  if(exRateBreakUp!=null)
		  { applDto.setLocalTranxAmount(exRateBreakUp.getConvertedLCAmount()); 
		  }
		 }
		 
		
		applDto.setCivilId(customer.getIdentityInt());
		applDto.setExchangeRate(placeOrder.getExchangeRateApplied());
		applDto.setOrignalExchangeRate(placeOrder.getRackExchangeRate());
		if(requestModelObject!=null) {
			applDto.setRoutingCountry(requestModelObject.getRoutingCountryId());
			applDto.setRoutingBankId(requestModelObject.getDynamicRroutingPricingBreakup().getTrnxRoutingPaths().getRoutingBankId());
			applDto.setRoutingBankName(requestModelObject.getDynamicRroutingPricingBreakup().getTrnxRoutingPaths().getRoutingBankCode());
		}
		
		if(placeOrder.getIsActive()!=null && placeOrder.getIsActive().equalsIgnoreCase(ConstantDocument.Status.U.toString())) {
			applDto.setStatus(ConstantDocument.Statusd.NEW.toString());
		}else if(placeOrder.getIsActive()!=null && placeOrder.getIsActive().equalsIgnoreCase(ConstantDocument.Status.Y.toString())) {
				applDto.setStatus(ConstantDocument.Statusd.APPROVED.toString());
			}
		
		
		list.add(applDto);
	}
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return list;
	
}

private ExchangeRateBreakup getExchangeRateBreakUPForPlaceOrder(RatePlaceOrder placeOrder) {
	ExchangeRateBreakup exRateBreakUp = new ExchangeRateBreakup();
	
	if(placeOrder!=null) {
	
	if(JaxUtil.isNullZeroBigDecimalCheck(placeOrder.getRequestCurrencyId()) && placeOrder.getRequestCurrencyId().compareTo(metaData.getDefaultCurrencyId())==0) {
		 exRateBreakUp.setConvertedLCAmount(placeOrder.getTransactionAmount());
		 exRateBreakUp = exchangeRateService.createBreakUp(placeOrder.getRateOffered()==null?placeOrder.getExchangeRateApplied():placeOrder.getRateOffered(), placeOrder.getTransactionAmount());
		 if(exRateBreakUp!=null) {
			 exRateBreakUp.setConvertedFCAmount(exRateBreakUp.getConvertedFCAmount());
		 }
	}else {
		exRateBreakUp.setConvertedFCAmount(placeOrder.getTransactionAmount());
		exRateBreakUp = exchangeRateService.createBreakUpFromForeignCurrency(placeOrder.getRateOffered()==null?placeOrder.getExchangeRateApplied():placeOrder.getRateOffered(), placeOrder.getTransactionAmount());
		if(exRateBreakUp!=null) {
			exRateBreakUp.setConvertedLCAmount(exRateBreakUp.getConvertedLCAmount());
		}
		
	}
	
	if(exRateBreakUp!=null) {
		remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID", placeOrder.getDestinationCurrenyId());
		exRateBreakUp.setNetAmount(exRateBreakUp.getConvertedLCAmount());
		exRateBreakUp.setNetAmountWithoutLoyality(exRateBreakUp.getConvertedLCAmount());
		remittanceTransactionManager.applyCurrencyRoudingLogic(exRateBreakUp);
	}
	
	
	}
	
	
	return exRateBreakUp;
	
}



public void validatePlaceOrderRequest(BranchRemittanceApplRequestModel applRequestModel,BenificiaryListView beneficaryDetails) {
	CurrencyOtherInformation currInfo = null;
	if(applRequestModel!=null ) {
		CurrencyMasterMdlv1 currMast = new CurrencyMasterMdlv1();
		if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getLocalAmount())) {
			currMast.setCurrencyId(metaData.getDefaultCurrencyId());
			currInfo = currencyOtherInfoRepository.findByExCurrencyMasterAndIsActive(currMast, ConstantDocument.Status.Y.toString());
		}else if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getForeignAmount())) {
			currMast.setCurrencyId(beneficaryDetails.getCurrencyId());
			currInfo = currencyOtherInfoRepository.findByExCurrencyMasterAndIsActive(currMast, ConstantDocument.Status.Y.toString());
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Place Order amount is not valid");
		}
		
		
		if(currInfo!=null && JaxUtil.isNullZeroBigDecimalCheck(currInfo.getPlaceOrderLimit())) {
			if(applRequestModel.getLocalAmount()!=null && currInfo.getPlaceOrderLimit().compareTo(applRequestModel.getLocalAmount())<=0) {
				//Allow the trnx  
			}else if(applRequestModel.getForeignAmount()!=null && currInfo.getPlaceOrderLimit().compareTo(applRequestModel.getForeignAmount())<=0) {
				//Allow the trnx  
			}
			else {
				throw new GlobalException(JaxError.RATE_PLACE_ERROR,"The minimum limit for place order is :"+currInfo.getPlaceOrderLimit());
			}
			
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Place Order limit is not defined for the selected currency");
		}
		
		/** same trnx amount check for the same beneficairy **/
		BigDecimal trnxAmount = null;
		if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getLocalAmount())){
			trnxAmount = applRequestModel.getLocalAmount();
		}else if(JaxUtil.isNullZeroBigDecimalCheck(applRequestModel.getForeignAmount())){
			trnxAmount = applRequestModel.getForeignAmount();
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"Place Order amount is not valid");
		}
		
		List<RatePlaceOrder> listPo = ratePlaceOrderRepository.sameBeneTrnxAmtCheck(beneficaryDetails.getCustomerId(),beneficaryDetails.getBeneficiaryRelationShipSeqId(),trnxAmount,beneficaryDetails.getServiceGroupId());
		
		if(!listPo.isEmpty() && listPo.size()>=1) {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"The same bene and same  amount place order is already avaliable");
		}
		/** differnet  trnx amount check for the same beneficairy **/
		
		List<RatePlaceOrder> listPoDiffAmount = ratePlaceOrderRepository.sameBeneTrnxButDiffAmtCheck(beneficaryDetails.getCustomerId(),beneficaryDetails.getBeneficiaryRelationShipSeqId(),beneficaryDetails.getServiceGroupId());
		
		
		AuthenticationLimitCheckView authPoLimitchk =parameterService.getPlaceOrderLimitCheck(ConstantDocument.PO_LIMIT_CHK);
		BigDecimal authLimitForDiffBene  = new BigDecimal(0);
		BigDecimal diffPlaceOrderCount   = new BigDecimal(0);
		if(authPoLimitchk!=null && JaxUtil.isNullZeroBigDecimalCheck(authPoLimitchk.getAuthLimit())) {
			authLimitForDiffBene = authPoLimitchk.getAuthLimit();
		}else {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"The same bene and differnt amount place order limit is not set");
		}
		
		if(listPoDiffAmount!=null && !listPoDiffAmount.isEmpty()) {
			diffPlaceOrderCount =  new BigDecimal(listPoDiffAmount.size());
		}
		
		if(JaxUtil.isNullZeroBigDecimalCheck(diffPlaceOrderCount) && JaxUtil.isNullZeroBigDecimalCheck(authLimitForDiffBene) && diffPlaceOrderCount.compareTo(authLimitForDiffBene)<0) {
			throw new GlobalException(JaxError.RATE_PLACE_ERROR,"The same bene and differnt amount place order limit is exceded");
		}
		
	}
	
}
	
public DynamicRoutingPricingDto acceptPlaceOrderByCustomer(BigDecimal ratePlaceOrderId) {
	
	RatePlaceOrder ratePlaceOrder = ratePlaceOrderRepository.fetchApprovedPlaceOrder(metaData.getCustomerId(),ratePlaceOrderId);
	 ObjectMapper mapper = new ObjectMapper();
	 DynamicRoutingPricingDto dyRoutingPricingdto = new DynamicRoutingPricingDto();
try {
	 
	if(ratePlaceOrder!=null ) {
	String requestJson = ratePlaceOrder.getRequestModel();
	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	BranchRemittanceApplRequestModel requestModelObject = mapper.readValue(requestJson, BranchRemittanceApplRequestModel.class);
	dyRoutingPricingdto =requestModelObject.getDynamicRroutingPricingBreakup();
	
	ExchangeRateBreakup exRateBreakUp = getExchangeRateBreakUPForPlaceOrder(ratePlaceOrder);
	exRateBreakUp.setInverseRate(ratePlaceOrder.getRateOffered());
	dyRoutingPricingdto.setExRateBreakup(exRateBreakUp);
	
	}else {
		throw new GlobalException(JaxError.RATE_PLACE_ERROR,"No record found");
	}
	
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
return dyRoutingPricingdto;
}



	public String getCustomerFullName(Customer customer){
		String customerName =null;

		if(customer !=null){
			if(customer.getFirstName() !=null){
				customerName = customer.getFirstName(); 
			}
			if(!StringUtils.isEmpty(customer.getMiddleName())){
				customerName = customerName +" "+customer.getMiddleName();
			}
			if(!StringUtils.isEmpty(customer.getLastName())){
				customerName = customerName+ " "+ customer.getLastName();
			}
		}
		return customerName;
		}

}

