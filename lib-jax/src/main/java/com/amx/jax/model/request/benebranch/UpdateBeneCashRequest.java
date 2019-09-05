package com.amx.jax.model.request.benebranch;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.model.request.AbtractUpdateBeneDetailDto;
import com.amx.jax.swagger.ApiMockModelProperty;

public class UpdateBeneCashRequest extends AbtractUpdateBeneDetailDto {

	@ApiMockModelProperty(example = "94")
	private BigDecimal beneficaryCountryId;

	@ApiMockModelProperty(example = "4")
	private BigDecimal currencyId;

	@ApiMockModelProperty(example = "1")
	private BigDecimal serviceGroupId;// cash or bank

	@ApiMockModelProperty(example = "2520")
	private BigDecimal serviceProviderId; // service provider

	@ApiMockModelProperty(example = "2520")
	private BigDecimal agentId; // agent master

	@ApiMockModelProperty(example = "305789")
	private BigDecimal serviceProviderBranchId; // agent branch

	public BigDecimal getBeneficaryCountryId() {
		return beneficaryCountryId;
	}

	public void setBeneficaryCountryId(BigDecimal beneficaryCountryId) {
		this.beneficaryCountryId = beneficaryCountryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public BigDecimal getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(BigDecimal serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public BigDecimal getAgentId() {
		return agentId;
	}

	public void setAgentId(BigDecimal agentId) {
		this.agentId = agentId;
	}

	public BigDecimal getServiceProviderBranchId() {
		return serviceProviderBranchId;
	}

	public void setServiceProviderBranchId(BigDecimal serviceProviderBranchId) {
		this.serviceProviderBranchId = serviceProviderBranchId;
	}

	@Override
	protected BeneAccountModel createBeneAccountModelObject() {
		BeneAccountModel model = new BeneAccountModel();
		try {
			BeanUtils.copyProperties(model, this);
			model.setBankId(this.getAgentId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return model;
	}

}
