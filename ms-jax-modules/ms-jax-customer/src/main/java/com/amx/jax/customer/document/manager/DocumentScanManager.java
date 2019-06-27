package com.amx.jax.customer.document.manager;

import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.model.customer.CustomerDocumentInfo;

public interface DocumentScanManager {

	CustomerDocumentInfo fetchKycImageInfo(CustomerIdProof customerIdProof);

	CustomerDocumentInfo getDocumentInfo(CustomerDocumentUploadReference upload);

}
