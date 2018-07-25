package com.amx.jax.placeorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
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
		List<PlaceOrder> placeOrderList = placeOrderAlertDao.getPlaceOrderAlertRate(countryId, currencyId, bankId,
				derivedSellRate);
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
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
			placeOrderDetailsAll(placeOrderList);
		 }
		response.getData().getValues().addAll(dtoList);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType("place-order-dto");
		
		return response;
		}

	private void placeOrderDetailsAll(List<PlaceOrder> placeOrderList) {
		int batchSize = 100;
		for (int i = 0; i < placeOrderList.size(); i += batchSize) {
			int endIndex = (i + batchSize);
			if (endIndex >= placeOrderList.size()) {
				endIndex = placeOrderList.size() - 1;
			}
			placeOrderDetails(placeOrderList.subList(i, endIndex + 1));
		}
	}
	
	private void placeOrderDetails(List<PlaceOrder> placeOrderList) {
		for (PlaceOrder placeorder : placeOrderList) {
			placeorder.setNotificationDate(new Date());
			iPlaceoderAlertRate.save(placeorder);
			Customer cusotmer= placeOrderCustomerDetails.getPlaceOrderCustomerDetails(placeorder.getCustomerId());
		    LOGGER.info("customer ID:" + placeorder.getCustomerId());
			PlaceOrderNotificationDTO placeorderDTO =new PlaceOrderNotificationDTO();
			placeorderDTO.setFirstName(cusotmer.getFirstName());
			placeorderDTO.setMiddleName(cusotmer.getMiddleName());
			placeorderDTO.setLastName(cusotmer.getLastName());
			placeorderDTO.setEmail(cusotmer.getEmail());
			placeorderDTO.setInputAmount(placeorder.getPayAmount());
			placeorderDTO.setOutputAmount(placeorder.getReceiveAmount());
			placeorderDTO.setRate(placeorder.getTargetExchangeRate());
			notificationService.sendBatchNotification(placeorderDTO);
			LOGGER.info("place Order:" + placeorderDTO.toString());
		}
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}
}
