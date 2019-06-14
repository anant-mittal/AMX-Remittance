package com.amx.jax.model.response.remittance;

import com.amx.jax.pricer.dto.TrnxRoutingDetails;

public class DynamicRoutingPricingDto extends RemittanceTransactionResponsetModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TrnxRoutingDetails trnxRoutingPaths;
	
	public TrnxRoutingDetails getTrnxRoutingPaths() {
		return trnxRoutingPaths;
	}
	public void setTrnxRoutingPaths(TrnxRoutingDetails trnxRoutingPaths) {
		this.trnxRoutingPaths = trnxRoutingPaths;
	}
	
}
