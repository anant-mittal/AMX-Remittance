package com.amx.jax.repository.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedCustomerDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

public interface ComplianceBlockedCustomerDocMapRepo extends CrudRepository<ComplianceBlockedCustomerDocMap, Serializable> {

	List<ComplianceBlockedCustomerDocMap> findByDocTypeMasterAndStatus(CustomerDocumentTypeMaster doctypeMaster, ComplianceTrnxdDocStatus status);

	List<ComplianceBlockedCustomerDocMap> findByDocTypeMasterAndStatusAndCustomerId(CustomerDocumentTypeMaster doctypeMaster,
			ComplianceTrnxdDocStatus status, BigDecimal customerId);
}
