package com.amx.jax.model.response.remittance;

import com.amx.jax.pricer.dto.TrnxRoutingDetails;

public class DynamicRoutingPricingDto extends RemittanceTransactionResponsetModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TrnxRoutingDetails trnxRoutingPaths;
	String beneDeductFlag;
	private ServiceProviderDto serviceProviderDto;
	
	public ServiceProviderDto getServiceProviderDto() {
		return serviceProviderDto;
	}
	public void setServiceProviderDto(ServiceProviderDto serviceProviderDto) {
		this.serviceProviderDto = serviceProviderDto;
	}
	public TrnxRoutingDetails getTrnxRoutingPaths() {
		return trnxRoutingPaths;
	}
	public void setTrnxRoutingPaths(TrnxRoutingDetails trnxRoutingPaths) {
		this.trnxRoutingPaths = trnxRoutingPaths;
	}
	public String getBeneDeductFlag() {
		return beneDeductFlag;
	}
	public void setBeneDeductFlag(String beneDeductFlag) {
		this.beneDeductFlag = beneDeductFlag;
	}
}
