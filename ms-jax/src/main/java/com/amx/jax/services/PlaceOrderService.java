package com.amx.jax.services;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.amxlib.model.placeorder.PlaceOrderCustomer;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.PlaceOrderUtil;
import com.google.common.collect.Lists;

/**
 * @author Subodh Bhoir
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class PlaceOrderService extends AbstractService {
	
	private Logger logger = LoggerService.getLogger(getClass());
	
	@Autowired
	IPlaceOrderDao placeOrderdao;
	
	@Autowired
	CurrencyMasterService currencyService;

	@Autowired
	CustomerDao customerDao;

	@Autowired
	MetaData metaData;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;	
	
	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;
	@Autowired
	RemittanceTransactionService remittanceTransactionService ; 
	/**
	 * Saved place order
	 * @param dto
	 * @return
	 */
	public ApiResponse savePlaceOrder(PlaceOrderDTO dto) {
		ApiResponse response = getBlackApiResponse();
		
		BenificiaryListView poBene = null;
		
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal beneRealtionId = dto.getBeneficiaryRelationshipSeqId();
		
		if (beneRealtionId != null && beneRealtionId.compareTo(BigDecimal.ZERO) != 0) {
            poBene = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(customerId, applicationCountryId,beneRealtionId);
        }
				
		PlaceOrder placeOrderModel = PlaceOrderUtil.getPlaceOrderModel(dto);
		beneficiaryValidationService.validateBeneList(beneRealtionId);
		//dto.getBeneficiaryRelationshipSeqId();
		try {
			placeOrderModel.setCreatedDate(new Date());
			placeOrderModel.setIsActive("Y");
			
			placeOrderModel.setBankId(poBene.getBankId());
			placeOrderModel.setCountryId(poBene.getCountryId());
			placeOrderModel.setCurrencyId(poBene.getCurrencyId());
			if (placeOrderModel.getReceiveAmount() == null) {
				updateReceiveAmount(placeOrderModel);
			}
			placeOrderdao.save(placeOrderModel);
			response.setResponseStatus(ResponseStatus.OK);
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while saving Place Order.", e);
		}
		logger.info("Place Order saved for customer : " +customerId);       
		return response;
	}

	private void updateReceiveAmount(PlaceOrder placeOrderModel) {
		RemittanceTransactionRequestModel requestModel = new RemittanceTransactionRequestModel();
		requestModel.setAvailLoyalityPoints(false);
		requestModel.setBeneId(placeOrderModel.getBeneficiaryRelationshipSeqId());
		requestModel.setDomXRate(placeOrderModel.getTargetExchangeRate());
		requestModel.setLocalAmount(placeOrderModel.getPayAmount());
		ApiResponse<RemittanceTransactionResponsetModel> apiResponse = remittanceTransactionService
				.calcEquivalentAmount(requestModel);
		RemittanceTransactionResponsetModel result = apiResponse.getResult();
		BigDecimal equivalentFcAmount = result.getExRateBreakup().getConvertedFCAmount();
		placeOrderModel.setReceiveAmount(equivalentFcAmount);
	}

	/**
	 * Get Place Order list for Customer
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ApiResponse<PlaceOrderDTO> getPlaceOrderForCustomer(BigDecimal customerId) {
		
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		
		try {
			placeOrderList = placeOrderdao.getPlaceOrderForCustomer(customerId);
			
			logger.info("Place Order list size for customer " +placeOrderList.size());
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					placeDTO.setBankId(rec.getBankId());
					placeDTO.setCountryId(rec.getCountryId());
					placeDTO.setCurrencyId(rec.getCurrencyId());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}else {
				logger.info("Place Order list is empty for customer :  " +customerId);
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List by Customer", e);
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}

	/**
	 * Get All Place Order list
	 * @return
	 */
	public ApiResponse getAllPlaceOrder() {
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		try {
			placeOrderList = placeOrderdao.getAllPlaceOrder();
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					placeDTO.setBankId(rec.getBankId());
					placeDTO.setCountryId(rec.getCountryId());
					placeDTO.setCurrencyId(rec.getCurrencyId());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching All Place Order List", e);
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}
	
	public ApiResponse deletePlaceOrder(PlaceOrderDTO dto) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<PlaceOrder> placeOrderList = placeOrderdao.getPlaceOrderDelete(dto.getPlaceOrderId());
			if(!placeOrderList.isEmpty()) {
				PlaceOrder rec = placeOrderList.get(0);
				rec.setIsActive("N");
				rec.setUpdatedDate(new Date());
				
				placeOrderdao.save(rec);
			}else {
				throw new GlobalException("No record found");
			}
			response.setResponseStatus(ResponseStatus.OK);
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while deleting Place Order record.", e);
		}
		
		logger.info("Place order Deleted ");
		return response;
	}
	
	public ApiResponse<PlaceOrderDTO> getPlaceOrderForId(BigDecimal placeOrderId) {
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();	
		
		try {
			placeOrderList = placeOrderdao.getPlaceOrderForId(placeOrderId);
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					placeDTO.setBankId(rec.getBankId());
					placeDTO.setCountryId(rec.getCountryId());
					placeDTO.setCurrencyId(rec.getCurrencyId());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List By Id", e);	
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}

	public ApiResponse updatePlaceOrder(PlaceOrderDTO dto) {
		ApiResponse response = getBlackApiResponse();
		
		BenificiaryListView poBene = null;
		
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal beneRealtionId = dto.getBeneficiaryRelationshipSeqId();
		
		if (beneRealtionId != null && beneRealtionId.compareTo(BigDecimal.ZERO) != 0) {
            poBene = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(customerId, applicationCountryId,beneRealtionId);
        }
		
		beneficiaryValidationService.validateBeneList(beneRealtionId);
		try {
			List<PlaceOrder> placeOrderList = placeOrderdao.getPlaceOrderUpdate(dto.getPlaceOrderId());
			if(!placeOrderList.isEmpty()) {
				PlaceOrder rec = placeOrderList.get(0);
				
				rec.setIsActive("Y");
				rec.setUpdatedDate(new Date());
				rec.setCustomerId(dto.getCustomerId());
				rec.setBeneficiaryRelationshipSeqId(dto.getBeneficiaryRelationshipSeqId());
				rec.setBankRuleFieldId(dto.getBankRuleFieldId());
				rec.setSrlId(dto.getSrlId());
				rec.setTargetExchangeRate(dto.getTargetExchangeRate());
				rec.setSourceOfIncomeId(dto.getSourceOfIncomeId());
				rec.setValidFromDate(dto.getValidFromDate());
				rec.setValidToDate(dto.getValidToDate());
				rec.setPayAmount(dto.getPayAmount());
				rec.setReceiveAmount(dto.getReceiveAmount());
				//rec.setCreatedDate(dto.getCreatedDate());
				rec.setBaseCurrencyId(dto.getBaseCurrencyId());
				rec.setBaseCurrencyQuote(dto.getBaseCurrencyQuote());
				rec.setForeignCurrencyId(dto.getForeignCurrencyId());
				rec.setForeignCurrencyQuote(dto.getForeignCurrencyQuote());
				
				rec.setBankId(poBene.getBankId());
				rec.setCountryId(poBene.getCountryId());
				rec.setCurrencyId(poBene.getCurrencyId());
				if (rec.getReceiveAmount() == null) {
					updateReceiveAmount(rec);
				}
				placeOrderdao.save(rec);
				
			}else {
				throw new GlobalException("No record found");
			}
			response.setResponseStatus(ResponseStatus.OK);
		
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while updating Place Order record.", e);
		}
		return response;
	}
	
	public ApiResponse<PlaceOrderDTO> rateAlertPlaceOrder(BigDecimal pipsMasterId) {
		List<PlaceOrderNotificationDTO> dtoList = new ArrayList<PlaceOrderNotificationDTO>();
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		try {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Set<PlaceOrder> placeOrderList = new HashSet<>();
			Set<PlaceOrder> placeOrderList1 = placeOrderdao.getPlaceOrderAlertRate1(pipsMasterId);
			placeOrderList.addAll(placeOrderList1);
			Set<PlaceOrder> placeOrderList2 = placeOrderdao.getPlaceOrderAlertRate2(pipsMasterId);
			placeOrderList.addAll(placeOrderList2);
			stopWatch.stop();
			logger.info("total time taken to run place order db query:{} seconds",
					stopWatch.getLastTaskTimeMillis() / 1000);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
			String date = simpleDateFormat.format(new Date());

			SimpleDateFormat simpletimeFormat = new SimpleDateFormat("HH:MM a z");
			String time = simpletimeFormat.format(new Date());

			NumberFormat myFormat = NumberFormat.getInstance();
			myFormat.setGroupingUsed(true);

			List<BigDecimal> poCustomerIds = placeOrderList.stream().map(po -> po.getCustomerId()).distinct()
					.collect(Collectors.toList());
			List<BigDecimal> poIds = placeOrderList.stream().map(po -> po.getOnlinePlaceOrderId()).distinct()
					.collect(Collectors.toList());
			stopWatch.start();
			updatePlaceOrdersForNotification(poIds);
			stopWatch.stop();
			logger.info("total time taken to run save place order query query:{} seconds",
					stopWatch.getLastTaskTimeMillis() / 1000);
			stopWatch.start();
			List<PlaceOrderCustomer> poCustomers = customerDao.getPersonInfoById(poCustomerIds);
			stopWatch.stop();
			logger.info("total time taken to run get customer query by id db query:{} seconds", stopWatch.getLastTaskTimeMillis() / 1000);
			Map<BigDecimal, PlaceOrderCustomer> poCustomerMap = poCustomers.stream()
					.collect(Collectors.toMap(x -> x.getCustomerId(), x -> x));

			if (placeOrderList != null && !placeOrderList.isEmpty()) {
				for (PlaceOrder placeorder : placeOrderList) {

					PlaceOrderCustomer cusotmer = poCustomerMap.get(placeorder.getCustomerId());
					logger.debug("customer ID:" + placeorder.getCustomerId());
					PlaceOrderNotificationDTO placeorderNotDTO = new PlaceOrderNotificationDTO();
					placeorderNotDTO.setFirstName(cusotmer.getFirstName());
					placeorderNotDTO.setMiddleName(cusotmer.getMiddleName());
					placeorderNotDTO.setLastName(cusotmer.getLastName());
					placeorderNotDTO.setEmail(cusotmer.getEmail());
					placeorderNotDTO.setInputAmount(myFormat.format(placeorder.getPayAmount()));
					placeorderNotDTO.setOutputAmount(myFormat.format(placeorder.getReceiveAmount()));
					placeorderNotDTO.setInputCur(placeorder.getBaseCurrencyQuote());
					placeorderNotDTO.setOutputCur(placeorder.getForeignCurrencyQuote());
					placeorderNotDTO.setRate(placeorder.getTargetExchangeRate());
					placeorderNotDTO.setOnlinePlaceOrderId(placeorder.getOnlinePlaceOrderId());
					placeorderNotDTO.setDate(date);
					placeorderNotDTO.setTime(time);
					placeorderNotDTO.setCustomerId(placeorder.getCustomerId());
					dtoList.add(placeorderNotDTO);

					placeorder.setUpdatedDate(new Date());
					placeorder.setNotificationDate(new Date());
				}
			}
			logger.debug("place Order for Notfication :" + dtoList.toString());

			response.getData().getValues().addAll(dtoList);
			response.setResponseStatus(ResponseStatus.OK);
			response.getData().setType("place-order-not-dto");

		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List by Trigger Exchange Rate", e);
		}
		return response;
	}
	
	private void updatePlaceOrdersForNotification(List<BigDecimal> poIds) {

		List<List<BigDecimal>> partitions = Lists.partition(poIds, 999);
		for (List<BigDecimal> parition : partitions) {
			placeOrderdao.updatePlaceOrdersForRateAlert(parition, Calendar.getInstance().getTime());
		}
	}

	public void savePlaceOrder(List<PlaceOrder> placeOrders) {
		placeOrderdao.save(placeOrders);
	}
	
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void validatePlaceOrderDto(PlaceOrderDTO dto) {
		// both foreign and domestic amounts should not be null
		if(dto.getPayAmount() == null && dto.getReceiveAmount() == null) {
			throw new GlobalException(JaxError.PO_BOTH_PAY_RECEIVED_AMT_NULL,
					"Both PayAmount and ReceivedAmount should not be null ");
		}
		
		/*if(dto.getPayAmount() != null && dto.getReceiveAmount() != null) {
			throw new GlobalException("Either PayAmount or ReceivedAmount should have value ",
					JaxError.PO_BOTH_PAY_RECEIVED_AMT_VALUE);
		}*/
	}
}
