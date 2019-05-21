package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.partner.dbmodel.BenificiaryListView;

public interface IBeneficiaryViewRepository extends CrudRepository<BenificiaryListView, Serializable> {
	
	public BenificiaryListView findByCustomerIdAndBeneficiaryRelationShipSeqId(BigDecimal customerId,BigDecimal beneficiaryRelationShipSeqId);

}
