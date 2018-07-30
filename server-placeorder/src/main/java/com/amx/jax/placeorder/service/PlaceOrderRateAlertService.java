package com.amx.jax.placeorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.placeorder.dao.PlaceOrderAlertDao;
import com.amx.jax.placeorder.dao.PlaceOrderNotificationDTO;
import com.amx.jax.placeorder.repository.IPlaceOrderCustomerDetails;
import com.amx.jax.placeorder.repository.IPlaceoderAlertRate;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class PlaceOrderRateAlertService extends AbstractService{
     
	@Autowired
	PlaceOrderAlertDao placeOrderAlertDao;
	
	@Autowired
	IPlaceoderAlertRate iPlaceoderAlertRate;
	
	@Autowired
	IPlaceOrderCustomerDetails placeOrderCustomerDetails;
	
	@Autowired
	NotificationService notificationService;
	
	private static final Logger LOGGER = Logger.getLogger(PlaceOrderRateAlertService.class);
	
	public ApiResponse<PlaceOrderDTO> rateAlertPlaceOrder(BigDecimal fromAmount,BigDecimal toAmount,BigDecimal countryId,BigDecimal currencyId,BigDecimal bankId ,BigDecimal derivedSellRate) {
		
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		try {
		List<PlaceOrder> placeOrderList = placeOrderAlertDao.getPlaceOrderAlertRate(countryId, currencyId, bankId,
				derivedSellRate);
		if (placeOrderList != null && !placeOrderList.isEmpty()) {
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
				
				dtoList.add(placeDTO);
			} 
			placeOrderDetailsAll(placeOrderList,derivedSellRate);
		 }
		response.getData().getValues().addAll(dtoList);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType("place-order-dto");
		
        } catch (Exception e) {
	    response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
	    LOGGER.error("Error while fetching Place Order List by Trigger Exchange Rate");
	    e.printStackTrace();
        } 
		return response;
	}

	private void placeOrderDetailsAll(List<PlaceOrder> placeOrderList, BigDecimal derivedSellRate) {
		int batchSize = 100;
		for (int i = 0; i < placeOrderList.size(); i += batchSize) {
			int endIndex = (i + batchSize);
			if (endIndex >= placeOrderList.size()) {
				endIndex = placeOrderList.size() - 1;
			}
			placeOrderDetails(placeOrderList.subList(i, endIndex + 1), derivedSellRate);
		}
	}
	
	private void placeOrderDetails(List<PlaceOrder> placeOrderList,  BigDecimal derivedSellRate) {
		derivedSellRate  = BigDecimal.ONE.divide(derivedSellRate, 3, RoundingMode.HALF_UP);
		for (PlaceOrder placeorder : placeOrderList) {
			placeorder.setNotificationDate(new Date());
			iPlaceoderAlertRate.save(placeorder);
			Customer cusotmer= placeOrderCustomerDetails.getPlaceOrderCustomerDetails(placeorder.getCustomerId());
		    LOGGER.info("customer ID:" + placeorder.getCustomerId());
			PlaceOrderNotificationDTO placeorderNotDTO =new PlaceOrderNotificationDTO();
			placeorderNotDTO.setFirstName(cusotmer.getFirstName());
			placeorderNotDTO.setMiddleName(cusotmer.getMiddleName());
			placeorderNotDTO.setLastName(cusotmer.getLastName());
			placeorderNotDTO.setEmail(cusotmer.getEmail());
			placeorderNotDTO.setInputAmount(placeorder.getPayAmount());
			placeorderNotDTO.setOutputAmount(placeorder.getReceiveAmount());
			placeorderNotDTO.setRate(derivedSellRate);
			placeorderNotDTO.setOnlinePlaceOrderId(placeorder.getOnlinePlaceOrderId());
			notificationService.sendBatchNotification(placeorderNotDTO);
			LOGGER.info("place Order:" + placeorderNotDTO.toString());
		}
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String setExchangeRate()
	{
		return "******------- Service is up ------*******";
	}
}
