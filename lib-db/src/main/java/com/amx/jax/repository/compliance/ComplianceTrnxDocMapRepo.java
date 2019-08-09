package com.amx.jax.repository.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

public interface ComplianceTrnxDocMapRepo extends CrudRepository<ComplianceBlockedTrnxDocMap, Serializable> {

	List<ComplianceBlockedTrnxDocMap> findByRemittanceTransaction(BigDecimal trnxId);

	List<ComplianceBlockedTrnxDocMap> findByDocTypeMasterAndCustomerId(CustomerDocumentTypeMaster docTypeMaster, BigDecimal customerId);

	List<ComplianceBlockedTrnxDocMap> findByDocTypeMasterAndCustomerIdAndStatus(CustomerDocumentTypeMaster docTypeMaster, BigDecimal customerId,
			ComplianceTrnxdDocStatus status);
}
