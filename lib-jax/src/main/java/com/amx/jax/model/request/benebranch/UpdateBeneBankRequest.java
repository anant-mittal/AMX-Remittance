package com.amx.jax.model.request.benebranch;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.model.request.AbtractUpdateBeneDetailDto;
import com.amx.jax.swagger.ApiMockModelProperty;

public class UpdateBeneBankRequest extends AbtractUpdateBeneDetailDto {

	// account detail
	@ApiMockModelProperty(example = "91")
	private BigDecimal beneficaryCountryId;

	@ApiMockModelProperty(example = "2258")
	private BigDecimal bankId;

	@ApiMockModelProperty(example = "247822")
	private BigDecimal bankBranchId;

	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid Account Number, only alphanumeric allowed")
	@ApiMockModelProperty(example = "67543227825")
	private String bankAccountNumber;

	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid ibanNumber, only alphanumeric allowed")
	@Size(min = 1, max = 60)
	private String ibanNumber;

	@ApiMockModelProperty(example = "11")
	private BigDecimal currencyId;

	@ApiMockModelProperty(example = "4")
	private BigDecimal bankAccountTypeId;

	public BigDecimal getBeneficaryCountryId() {
		return beneficaryCountryId;
	}

	public void setBeneficaryCountryId(BigDecimal beneficaryCountryId) {
		this.beneficaryCountryId = beneficaryCountryId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
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
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return model;
	}

}
