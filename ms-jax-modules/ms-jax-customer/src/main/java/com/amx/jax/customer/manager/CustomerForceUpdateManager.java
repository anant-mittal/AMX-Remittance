package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedCustomerDocMap;
import com.amx.jax.model.response.customer.CustomerForceUpdateModel;
import com.amx.jax.model.response.customer.CustomerMgmtUIField;
import com.amx.jax.model.response.customer.DocumentUploadMeta;
import com.amx.jax.userservice.service.UserService;

@Component
public class CustomerForceUpdateManager {

	@Autowired
	UserService userService;

	public List<CustomerForceUpdateModel> getBlockedCustomerModel(BigDecimal customerId) {
		List<CustomerForceUpdateModel> customerForceUpdateModel = new ArrayList<>();
		Customer customer = userService.getCustById(customerId);
		List<ComplianceBlockedCustomerDocMap> blockedDocuments = customer.getComplianceBlockedDocuments();
		blockedDocuments.forEach(i -> {
			CustomerForceUpdateModel model = new CustomerForceUpdateModel();
			model.setField(CustomerMgmtUIField.DOCUMENT);
			DocumentUploadMeta documentUploadMeta = new DocumentUploadMeta();
			documentUploadMeta.setCustomerDocTypeMasterId(i.getId());
			model.setMeta(documentUploadMeta);
			customerForceUpdateModel.add(model);
		});
		return customerForceUpdateModel;
	}
}
