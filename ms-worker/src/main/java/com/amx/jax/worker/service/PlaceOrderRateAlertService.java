package com.amx.jax.worker.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.client.PlaceOrderClient;

@Service
public class PlaceOrderRateAlertService {

	@Autowired
	PlaceOrderClient placeOrderClient;

	@Autowired
	NotificationService notificationService;

	private static final Logger LOGGER = Logger.getLogger(PlaceOrderRateAlertService.class);

	public ApiResponse<PlaceOrderNotificationDTO> rateAlertPlaceOrder(BigDecimal fromAmount, BigDecimal toAmount,
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal derivedSellRate) {

		ApiResponse<PlaceOrderNotificationDTO> response = ApiResponse.getBlackApiResponse();
		try {
			List<PlaceOrderNotificationDTO> placeOrderList = placeOrderClient
					.getPlaceOrderOnTrigger(fromAmount, toAmount, countryId, currencyId, bankId, derivedSellRate)
					.getResults();
			if (placeOrderList != null && !placeOrderList.isEmpty()) {
				placeOrderDetails(placeOrderList);
			}
			response.getData().getValues().addAll(placeOrderList);
			response.setResponseStatus(ResponseStatus.OK);
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			LOGGER.error("Error while fetching Place Order List by Trigger Exchange Rate");
			e.printStackTrace();
		}
		return response;
	}

	private void placeOrderDetails(List<PlaceOrderNotificationDTO> placeOrderList) {
		int batchSize = 10;
		for (int i = 0; i < placeOrderList.size(); i += batchSize) {
			int endIndex = (i + batchSize);
			if (endIndex >= placeOrderList.size()) {
				endIndex = placeOrderList.size() - 1;
			}
			notificationService.sendBatchNotification(placeOrderList.subList(i, endIndex + 1));
		}
	}

}
