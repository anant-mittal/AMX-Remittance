package com.amx.jax.repository.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.compliance.ComplianceTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

public interface ComplianceTrnxDocMapRepo extends CrudRepository<ComplianceTrnxDocMap, Serializable> {

	List<ComplianceTrnxDocMap> findByRemittanceTransaction(BigDecimal trnxId);

	List<ComplianceTrnxDocMap> findByDocTypeMasterAndCustomerId(CustomerDocumentTypeMaster docTypeMaster, BigDecimal customerId);
}
