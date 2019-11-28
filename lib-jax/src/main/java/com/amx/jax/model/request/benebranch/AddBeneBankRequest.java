package com.amx.jax.model.request.benebranch;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.swagger.ApiMockModelProperty;

public class AddBeneBankRequest extends AbstractBeneDetailDto {

	// account detail
	@NotNull(message = "Beneficary Country Id may not be null")
	@ApiMockModelProperty(example = "91")
	private BigDecimal benificaryCountry;

	@NotNull(message = "Bank Id may not be null")
	@ApiMockModelProperty(example = "2258")
	private BigDecimal bankId;

	@NotNull(message = "Bank branch may not be null")
	@ApiMockModelProperty(example = "247822")
	private BigDecimal branchId;

	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid Account Number, only alphanumeric allowed")
	@NotNull
	@ApiMockModelProperty(example = "67543227825")
	private String bankAccountNumber;

	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid ibanNumber, only alphanumeric allowed")
	@Size(min = 1, max = 60)
	private String ibanNumber;

	@NotNull(message = "Currency Id may not be null")
	@ApiMockModelProperty(example = "11")
	private BigDecimal currencyId;

	@NotNull(message = "bankAccountTypeId may not be null")
	@ApiMockModelProperty(example = "4")
	private BigDecimal bankAccountTypeId;

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getIbanNumber() {
		return ibanNumber;
	}

	public void setIbanNumber(String ibanNumber) {
		this.ibanNumber = ibanNumber;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getBankAccountTypeId() {
		return bankAccountTypeId;
	}

	public void setBankAccountTypeId(BigDecimal bankAccountTypeId) {
		this.bankAccountTypeId = bankAccountTypeId;
	}

	@Override
	protected BeneAccountModel createBeneAccountModelObject() {
		BeneAccountModel model = new BeneAccountModel();
		try {
			BeanUtils.copyProperties(model, this);
			model.setBankBranchId(this.getBranchId());
			model.setBeneficaryCountryId(this.getBenificaryCountry());
			model.setSwiftCode(this.getSwiftBic());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return model;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getBenificaryCountry() {
		return benificaryCountry;
	}

	public void setBenificaryCountry(BigDecimal benificaryCountry) {
		this.benificaryCountry = benificaryCountry;
	}

}
