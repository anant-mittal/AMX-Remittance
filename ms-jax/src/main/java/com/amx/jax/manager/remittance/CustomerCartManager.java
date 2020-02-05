package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dao.CustomerCartDao;
import com.amx.jax.dbmodel.CustomerCartMaster;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.CustomerCartRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class CustomerCartManager extends AbstractModel {

	@Autowired
	CustomerCartDao customerCartDao;

	@Autowired
	CustomerCartRepository customerCartRepository;
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;

	Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	public void addToCustomerCart(RemittanceApplication remittanceApplication) {

		BigDecimal custId = remittanceApplication.getFsCustomer().getCustomerId();
		CustomerCartMaster cartData = customerCartDao.getCartData(custId);

		if (cartData != null) {
			if (cartData.getApplIds() != null) {
				String applicationIds = cartData.getApplIds();
				applicationIds = applicationIds + "," + remittanceApplication.getRemittanceApplicationId().toString();
				cartData.setApplIds(applicationIds);
				cartData.setModifiedDate(new Date());
			} else {
				cartData.setApplIds(remittanceApplication.getRemittanceApplicationId().toString());
				cartData.setModifiedDate(new Date());
			}

			cartData.setLinkCount(null);
			customerCartRepository.save(cartData);
		} else {
			CustomerCartMaster cartDataNew = new CustomerCartMaster();
			cartDataNew.setCustomerId(custId);
			cartDataNew.setApplIds(remittanceApplication.getRemittanceApplicationId().toString());
			cartDataNew.setCreatedDate(new Date());

			customerCartRepository.save(cartDataNew);
		}
		
		// CART_ID STORE IN EX_APPL_TRNX ------
		BigDecimal remittanceAppliId = remittanceApplication.getRemittanceApplicationId();
		CustomerCartMaster cartDataForId = customerCartDao.getCartDataByApplId(custId, remittanceAppliId);
		if(cartDataForId != null) {
			remittanceApplicationRepository.updateCartId(remittanceAppliId, cartDataForId.getCartId());
		}
	}

	public void deleteInCustomerCart(BigDecimal customerId, BigDecimal remittanceAppliId) {

		CustomerCartMaster cartData = customerCartDao.getCartDataByApplId(customerId, remittanceAppliId);

		if (cartData != null) {
			String applIds = cartData.getApplIds();

			List<String> applList = Arrays.asList(applIds.split(","));
			List<String> applListCopy = new ArrayList<>(applList);
			logger.info("Application Ids Before Remove : " + applListCopy.toString());

			applListCopy.remove(new String(remittanceAppliId.toString()));
			logger.info("Application Ids After Remove : " + applListCopy.toString());
			if(applListCopy != null && !applListCopy.isEmpty()) {
				String applListString = String.join(",", applListCopy);
				cartData.setApplIds(applListString);
			}else {
				cartData.setApplIds(null);
			}
			
			cartData.setLinkCount(null);
			cartData.setModifiedDate(new Date());

			customerCartRepository.save(cartData);

		}
	}

}
