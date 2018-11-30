package com.amx.jax.manager;
/**
 * @author rabil
 *
 */
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;








import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dal.LoyaltyInsuranceProDao;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.FxDeliveryReportDetailDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.FxOrderTransactionStatusResponseDto;
import com.amx.jax.model.response.fx.PaygDetailsDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxOrderTransactionRespository;
import com.amx.jax.util.DateUtil;



@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FxOrderReportManager {
	@Autowired
	MetaData metaData;
	
	@Autowired
	FxOrderTransactionRespository fxTransactionHistroyDao;
	
	@Autowired
	FcSaleApplicationTransactionManager applTrnxManager;
	
	@Autowired
	FxDeliveryDetailsRepository deliveryDetailsRepos;
	
	@Autowired
	PaygDetailsRepository payGDeatilsRepos;
	
	@Autowired
	IShippingAddressRepository shippingAddressDao;
	
	
	@Autowired
	ICustomerRepository customerDao;
	
	
	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	private MetaData meta;
	
	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;
	
	@Autowired
	IViewDistrictDAO districtDao;
	
	@Autowired
	IViewArea areaDao;
	
	@Autowired
	ReceiptPaymentAppRepository rcptPaymentAppl;
	
	@Autowired
	LoyaltyInsuranceProDao loyaltyInsuranceProDao;
	
	/**
	 * 
	 * @param collNo
	 * @param collFyr
	 * @return
	 */
	public FxOrderReportResponseDto getReportDetails(BigDecimal collNo,BigDecimal collFyr){
		FxOrderReportResponseDto reportModel = new FxOrderReportResponseDto();
		BigDecimal custoemrId = metaData.getCustomerId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal customerReferenceId = BigDecimal.ZERO;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String createdDate=""; 
		String receiptNo = "";
		String customerName="";
		String phoneNo ="";
		
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, custoemrId);
		if(!customerList.isEmpty()){
			reportModel.setIdExpiryDate(DateUtil.todaysDateWithDDMMYY(customerList.get(0).getIdentityExpiredDate(),"0"));
			reportModel.setCivilId(customerList.get(0).getIdentityInt());
			customerReferenceId = customerList.get(0).getCustomerReference();
			phoneNo =  customerList.get(0).getMobile();
		}
		
		List<FxOrderTransactionModel> fxOrderTrnxList =  fxTransactionHistroyDao.getFxOrderTrnxListByCollectionDocNumber(custoemrId,collNo,collFyr);
	    if(!fxOrderTrnxList.isEmpty()){
	    	List<FxOrderTransactionHistroyDto> fxOrderTrnxListDto = applTrnxManager.convertFxHistDto(fxOrderTrnxList);
	    	reportModel.setFxOrderTrnxList(fxOrderTrnxListDto);
	    	BigDecimal deliveryDetSeqId =fxOrderTrnxList.get(0).getDeliveryDetSeqId() ;
	    	BigDecimal paygSeqId        = fxOrderTrnxList.get(0).getPagDetSeqId();
	    	if(fxOrderTrnxList.get(0).getCollectionDocumentNo()!=null && fxOrderTrnxList.get(0).getCollectionDocumentFinYear()!=null){
	    		receiptNo =  fxOrderTrnxList.get(0).getCollectionDocumentFinYear().toString()+"/"+fxOrderTrnxList.get(0).getCollectionDocumentNo().toString();
	    	}
	    	customerName=fxOrderTrnxList.get(0).getCustomerName(); 
	    	FxDeliveryDetailsModel fxDelDetailModel = deliveryDetailsRepos.findOne(deliveryDetSeqId);
	    	PaygDetailsModel pgDetailsModel = payGDeatilsRepos.findOne(paygSeqId);
	    	createdDate = fxOrderTrnxList.get(0).getCreatedDate();
	    	
	    	if(fxDelDetailModel!=null){
	    		FxDeliveryReportDetailDto delDto = convertFxDeliveryDto(fxDelDetailModel);
	    		reportModel.setDeliveryDetailReport(delDto);
	    		ShippingAddressDetail shippAddDetails = shippingAddressDao.findOne(fxDelDetailModel.getShippingAddressId());
	    		ShippingAddressDto shippingAddressDto = getShippingaddressDetails(shippAddDetails);
	    		reportModel.setShippingAddressdto(shippingAddressDto);
	    	}
	    	if(pgDetailsModel!=null){
	    		PaygDetailsDto pgdto = convertFxPgDetailsDto(pgDetailsModel);
	    		reportModel.setPayg(pgdto);
	    	}
	    	
	    	reportModel.setReceiptNo(receiptNo);
	    	reportModel.setCustomerName(customerName);
	    	reportModel.setPhoneNumber(phoneNo);
	    	reportModel.setLocation(fxOrderTrnxList.get(0).getBranchDesc());
	    }
	    
	    

		Map<String, Object> loyaltiPoints = loyaltyInsuranceProDao.loyaltyInsuranceProcedure(customerReferenceId, createdDate);
		
		String prLtyStr1 =loyaltiPoints.get("P_LTY_STR1")==null?"":loyaltiPoints.get("P_LTY_STR1").toString();
		String prLtyStr2 =loyaltiPoints.get("P_LTY_STR2")==null?"":loyaltiPoints.get("P_LTY_STR2").toString();
		String prInsStr1 =loyaltiPoints.get("P_INS_STR1")==null?"":loyaltiPoints.get("P_INS_STR1").toString();
		String prInsStr2 =loyaltiPoints.get("P_INS_STR2")==null?"":loyaltiPoints.get("P_INS_STR2").toString();
		String prInsStrAr1 =loyaltiPoints.get("P_INS_STR_AR1")==null?"":loyaltiPoints.get("P_INS_STR_AR1").toString();
		String prInsStrAr2 =loyaltiPoints.get("P_INS_STR_AR2")==null?"":loyaltiPoints.get("P_INS_STR_AR2").toString();

		if(!prLtyStr1.trim().equals("") && !prLtyStr2.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr1+"  \n"+prLtyStr2);
		}else if(!prLtyStr1.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr1);
		}else if(!prLtyStr2.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr2);
		}

		 if(!prInsStr1.trim().equals("")){
			reportModel.setInsurence1(prInsStr1);
		}else if(!prInsStrAr1.trim().equals("")){
			reportModel.setInsurence1(prInsStrAr1);
		}
		if(!prInsStr2.trim().equals("") && !prInsStrAr2.trim().equals("")){
			reportModel.setInsurence2(prInsStr2+"  \n"+prInsStrAr2);
		}else if(!prInsStr2.trim().equals("")){
			reportModel.setInsurence2(prInsStr2);
		}else if(!prInsStrAr2.trim().equals("")){
			reportModel.setInsurence2(prInsStrAr2);
		}
	    
		return reportModel; 
	}
	
	/*
	 * To get Transaction status: 
	 */
	
	public FxOrderTransactionStatusResponseDto  getTransactionStatus(BigDecimal paymentSeqId){
		FxOrderTransactionStatusResponseDto responseModel = new FxOrderTransactionStatusResponseDto();
		FxOrderTransactionHistroyDto histroyDto = new FxOrderTransactionHistroyDto();
		BigDecimal custoemrId = metaData.getCustomerId();
		BigDecimal netAmount =BigDecimal.ZERO;
		BigDecimal deliveryCharges =BigDecimal.ZERO;
		ReceiptPaymentApp applReceipt = rcptPaymentAppl.getApplicationByPagdetailSeqIAndcustomerId(custoemrId, paymentSeqId);
		PaygDetailsModel pgDetailsModel = payGDeatilsRepos.findOne(paymentSeqId);
		FxDeliveryDetailsModel fxDelDetailModel = deliveryDetailsRepos.findOne(applReceipt.getDeliveryDetSeqId());
		JaxTransactionStatus jaxTrnxStatus = getJaxTransactionStatus(pgDetailsModel,applReceipt);
		if(applReceipt!=null){
		List<FxOrderTransactionModel> fxOrderTrnxList =  fxTransactionHistroyDao.getFxOrderTrnxListByCollectionDocNumber(custoemrId,applReceipt.getColDocNo(),applReceipt.getColDocFyr());
			 if(!fxOrderTrnxList.isEmpty()){
			    	List<FxOrderTransactionHistroyDto> fxOrderTrnxListDto = applTrnxManager.convertFxHistDto(fxOrderTrnxList);
			    	responseModel.setFxOrderTrnxHistroyDTO(fxOrderTrnxListDto);
			 }
		}
		if(applReceipt!=null){
			netAmount = netAmount.add(applReceipt.getLocalTrnxAmount());
		}
		if(fxDelDetailModel !=null){
			deliveryCharges = fxDelDetailModel.getDeliveryCharges();
		}	
		
		
		responseModel.setNetAmount(netAmount.add(deliveryCharges));
		responseModel.setStatus(jaxTrnxStatus);
		return responseModel;
	}
	
	
	 FxDeliveryDetailDto  deliveryDetailList = new FxDeliveryDetailDto();
	 ShippingAddressDto shippingAddressdto =new ShippingAddressDto();
	 PaygDetailsDto payg = new PaygDetailsDto();
	
	public FxDeliveryReportDetailDto convertFxDeliveryDto(FxDeliveryDetailsModel delDetails){
		FxDeliveryReportDetailDto  dto = new FxDeliveryReportDetailDto();
		try {
			BeanUtils.copyProperties(dto, delDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			
		}
		return dto;
		}
	
	public PaygDetailsDto convertFxPgDetailsDto(PaygDetailsModel pgdelDetails){
		PaygDetailsDto  dto = new PaygDetailsDto();
		try {
			BeanUtils.copyProperties(dto, pgdelDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			
		}
		return dto;
		}
	
	public ShippingAddressDto getShippingaddressDetails(ShippingAddressDetail shippingAddressDetail){
		ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
		BigDecimal countryId = meta.getCountryId();
		BigDecimal customerId = meta.getCustomerId();
		BigDecimal companyId = meta.getCompanyId();
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		if (shippingAddressDetail!=null) {
				
				shippingAddressDto.setAddressId(shippingAddressDetail.getShippingAddressDetailId());
				
				if(!customerList.isEmpty()){
					shippingAddressDto.setFirstName(customerList.get(0).getFirstName());
					shippingAddressDto.setMiddleName(customerList.get(0).getMiddleName());
					shippingAddressDto.setLastName(customerList.get(0).getLastName());
				}
			shippingAddressDto.setCustomerId(shippingAddressDetail.getFsCustomer().getCustomerId());
			shippingAddressDto.setCompanyId(companyId);
			shippingAddressDto.setMobile(shippingAddressDetail.getMobile());
			shippingAddressDto.setLocalContactBuilding(shippingAddressDetail.getBuildingNo());
			shippingAddressDto.setStreet(shippingAddressDetail.getStreet());
			shippingAddressDto.setBlockNo(shippingAddressDetail.getBlock());
			shippingAddressDto.setHouse(shippingAddressDetail.getFlat());
			shippingAddressDto.setAdressType(shippingAddressDetail.getAddressType());
			shippingAddressDto.setAreaDesc(areaDao.getAreaList(shippingAddressDetail.getAreaCode())==null?"":areaDao.getAreaList(shippingAddressDetail.getAreaCode()).getShortDesc());
			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),shippingAddressDetail.getFsCountryMaster().getCountryId());
			if (!countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (shippingAddressDetail.getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),shippingAddressDetail.getFsStateMaster().getStateId(), new BigDecimal(1));
					if (!stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						DistrictMaster distictMaster = shippingAddressDetail.getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),new BigDecimal(1));
							if (!districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),shippingAddressDetail.getFsCityMaster().getCityId(), new BigDecimal(1));
								if (!cityDetails.isEmpty()) {
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
								}
							}
						}
					}
				}

			}
	} //end 
		return shippingAddressDto;
	}
	
	
	private JaxTransactionStatus getJaxTransactionStatus(PaygDetailsModel pgDetailsModel,ReceiptPaymentApp applReceipt) {
		JaxTransactionStatus status = JaxTransactionStatus.APPLICATION_CREATED;
		String applicationStatus = applReceipt.getApplicationStatus();
		if (StringUtils.isBlank(applicationStatus) && pgDetailsModel.getPgPaymentId() != null) {
			status = JaxTransactionStatus.PAYMENT_IN_PROCESS;
		}
		String resultCode = pgDetailsModel.getResultCode();
		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			if ("S".equals(applicationStatus) || "T".equals(applicationStatus)) {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS;
			} else {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_FAIL;
			}
		}
		if ("NOT CAPTURED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_FAIL;
		}
		if ("CANCELED".equalsIgnoreCase(resultCode) || "CANCELLED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_CANCELED_BY_USER;
		}

		return status;
	}

	
}
