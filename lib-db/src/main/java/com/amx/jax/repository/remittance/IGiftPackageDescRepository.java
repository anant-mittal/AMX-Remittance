package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.GiftPackageDescModel;

public interface IGiftPackageDescRepository extends CrudRepository<GiftPackageDescModel, Serializable> {

	public GiftPackageDescModel findByRoutingBankIdAndAmiecCode(BigDecimal routingBankId, String amiecCode);
	public GiftPackageDescModel findByRoutingBankIdAndBeneficiaryBankIdAndBankBranchIdAndAmiecCode(BigDecimal routingBankId, BigDecimal beneficiaryBankId, BigDecimal bankBranchId, String amiecCode);
}
