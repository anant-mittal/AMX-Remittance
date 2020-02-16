package com.amx.jax.model.request.benebranch;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.swagger.ApiMockModelProperty;

public class AddBeneCashRequest extends AbstractBeneDetailDto {

	@NotNull(message = "Beneficary Country Id may not be null")
	@ApiMockModelProperty(example = "94")
	private BigDecimal benificaryCountry;

	@NotNull(message = "Currency Id may not be null")
	@ApiMockModelProperty(example = "4")
	private BigDecimal currencyId;

	@NotNull(message = "serviceProviderId may not be null")
	@ApiMockModelProperty(example = "2520")
	private BigDecimal serviceProvider; // service provider

	@NotNull(message = "Agent Id may not be null")
	@ApiMockModelProperty(example = "2520")
	private BigDecimal bankId; // agent master

	@NotNull(message = "serviceProviderBranchId may not be null")
	@ApiMockModelProperty(example = "305789")
	private BigDecimal branchId; // agent branch

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	protected BeneAccountModel createBeneAccountModelObject() {
		BeneAccountModel model = new BeneAccountModel();
		try {
			BeanUtils.copyProperties(model, this);
			model.setBankBranchId(this.getBranchId());
			model.setBeneficaryCountryId(this.getBenificaryCountry());
			model.setSwiftCode(this.getSwiftBic());
			model.setServiceProviderId(this.getServiceProvider());
			model.setServiceProviderBranchId(this.getBranchId());

		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return model;
	}

	public BigDecimal getBenificaryCountry() {
		return benificaryCountry;
	}

	public void setBenificaryCountry(BigDecimal benificaryCountry) {
		this.benificaryCountry = benificaryCountry;
	}

	public BigDecimal getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(BigDecimal serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}
}
