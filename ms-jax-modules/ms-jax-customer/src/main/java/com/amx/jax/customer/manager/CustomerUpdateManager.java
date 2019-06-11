package com.amx.jax.customer.manager;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;

@Component
public class CustomerUpdateManager {

	@Autowired
	CustomerEmployementManager customerEmployementManager;
	@Autowired
	CustomerPersonalDetailManager customerPersonalDetailManager;
	@Autowired
	CustomerAddressDetailsManager customerAddressDetailsManager;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	private static final Logger log = LoggerFactory.getLogger(CustomerUpdateManager.class);

	@Transactional
	public void updateCustomer(UpdateCustomerInfoRequest req) {
		log.info("updating customer info req: {}", JsonUtil.toJson(req));
		BigDecimal customerId = metaData.getCustomerId();
		Customer customer = userService.getCustById(customerId);
		if (req.getEmploymentDetail() != null) {
			customerEmployementManager.updateCustomerEmploymentInfo(customer, req.getEmploymentDetail());
		}
		if (req.getHomeAddressDetail() != null) {
			customerAddressDetailsManager.updateCustomerAddressDetail(customer, req.getHomeAddressDetail());
		}
		if (req.getLocalAddressDetail() != null) {
			customerAddressDetailsManager.updateCustomerAddressDetail(customer, req.getLocalAddressDetail());
		}
		if (req.getPersonalDetailInfo() != null) {
			customerPersonalDetailManager.updateCustomerPersonalDetail(customer, req.getPersonalDetailInfo());
		}
	}

}
