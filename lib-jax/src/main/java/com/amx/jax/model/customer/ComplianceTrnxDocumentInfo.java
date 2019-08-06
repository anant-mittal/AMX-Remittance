package com.amx.jax.model.customer;

import com.amx.jax.client.compliance.ComplianceTrnxStatus;

public class ComplianceTrnxDocumentInfo extends CustomerDocumentInfo {

	ComplianceTrnxStatus status;

	public ComplianceTrnxDocumentInfo() {

	}

	public ComplianceTrnxStatus getStatus() {
		return status;
	}

	public void setStatus(ComplianceTrnxStatus status) {
		this.status = status;
	}
}
