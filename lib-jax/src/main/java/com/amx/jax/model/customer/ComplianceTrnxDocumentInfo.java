package com.amx.jax.model.customer;

import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;

public class ComplianceTrnxDocumentInfo extends CustomerDocumentInfo {

	ComplianceTrnxdDocStatus status;

	public ComplianceTrnxDocumentInfo() {

	}

	public ComplianceTrnxdDocStatus getStatus() {
		return status;
	}

	public void setStatus(ComplianceTrnxdDocStatus status) {
		this.status = status;
	}
}
