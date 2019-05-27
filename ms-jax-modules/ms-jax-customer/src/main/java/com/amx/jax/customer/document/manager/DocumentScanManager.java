package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.model.customer.CustomerDocumentInfo;

public interface DocumentScanManager {

	CustomerDocumentInfo fetchKycImageInfo(CustomerIdProof customerIdProof);

	List<CustomerDocumentInfo> fetchOtherDocumentInfo(BigDecimal customerId);

}
