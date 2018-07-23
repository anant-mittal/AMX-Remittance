package com.amx.jax.placeorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.placeorder.controller.PlaceOrderController;
import com.amx.jax.placeorder.dao.PlaceOrderAlertDao;
import com.amx.jax.placeorder.repository.IPlaceOrderBeneRelationDao;
import com.amx.jax.placeorder.repository.IPlaceOrderCustomerDetails;

@Service
@SuppressWarnings("rawtypes")
public class PlaceOrderRateAlertService {
     
	@Autowired
	PlaceOrderAlertDao placeOrderAlertDao;
	
	@Autowired
	IPlaceOrderCustomerDetails placeOrderCustomerDetails;
	
	@Autowired
	NotificationService notificationService;
	
/*	@Autowired
	IPlaceOrderBeneRelationDao placeOrderBeneRelationDao;*/
	
	private static final Logger LOGGER = Logger.getLogger(PlaceOrderRateAlertService.class);
	
	public List<PlaceOrder> rateAlertPlaceOrder(BigDecimal fromAmount,BigDecimal toAmount,BigDecimal countryId,BigDecimal currencyId,BigDecimal bankId ,BigDecimal derivedSellRate) {
  	
		
		/*List<BenificiaryListView> benificiaryList =placeOrderBeneRelationDao.getPlaceOrderBeneRelation(countryId, currencyId, bankId);
		if(benificiaryList!=null && !benificiaryList.isEmpty()) {
			for(BenificiaryListView view : benificiaryList) {
				PlaceOrder placeOrder = new PlaceOrder();
				
			}
		}*/
		List customerIdList = new ArrayList();
		List<PlaceOrder> placeOrderList = placeOrderAlertDao.getPlaceOrderAlertRate(countryId, currencyId, bankId,
				derivedSellRate);
		if (placeOrderList != null && !placeOrderList.isEmpty()) {
			for (PlaceOrder placeorder : placeOrderList) {
				customerIdList.add(placeorder.getCustomerId());
			    LOGGER.info("customer ID:" + placeorder.getCustomerId());
				}
			customerDetails(customerIdList);
			}
		return placeOrderList;
		}

	private void customerDetails(List customerIdList) {
		int batchSize = 100;
		for (int i = 0; i < customerIdList.size(); i += batchSize) {
			int endIndex = (i + batchSize);
			if (endIndex >= customerIdList.size()) {
				endIndex = customerIdList.size() - 1;
			}
			List<Customer> cu= placeOrderCustomerDetails.getPlaceOrderCustomerDetailsAll(customerIdList.subList(i, endIndex + 1));
			notificationService.sendBatchNotification(cu);
			LOGGER.info("customer det:" + cu.toString());
		}
    }

}
