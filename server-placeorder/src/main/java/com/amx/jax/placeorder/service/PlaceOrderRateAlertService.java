package com.amx.jax.placeorder.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.placeorder.dao.PlaceOrderAlertDao;
import com.amx.jax.placeorder.dao.PlaceOrderNotificationDTO;
import com.amx.jax.placeorder.repository.IPlaceOrderCustomerDetails;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class PlaceOrderRateAlertService extends AbstractService{
     
	@Autowired
	PlaceOrderAlertDao placeOrderAlertDao;
	
	@Autowired
	IPlaceOrderCustomerDetails placeOrderCustomerDetails;
	
	@Autowired
	NotificationService notificationService;
	
	private static final Logger LOGGER = Logger.getLogger(PlaceOrderRateAlertService.class);
	
	public ApiResponse<PlaceOrder> rateAlertPlaceOrder(BigDecimal fromAmount,BigDecimal toAmount,BigDecimal countryId,BigDecimal currencyId,BigDecimal bankId ,BigDecimal derivedSellRate) {
		ApiResponse<PlaceOrder> response = getBlackApiResponse();
		List<PlaceOrder> placeOrderList = placeOrderAlertDao.getPlaceOrderAlertRate(countryId, currencyId, bankId,
				derivedSellRate);
		if (placeOrderList != null && !placeOrderList.isEmpty()) {
			  
			  placeOrderDetailsAll(placeOrderList);
			}
		/*return placeOrderList;*/
		
		response.getData().getValues().addAll(placeOrderList);
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
