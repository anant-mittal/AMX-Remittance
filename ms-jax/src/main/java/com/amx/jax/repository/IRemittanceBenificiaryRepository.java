package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

public interface IRemittanceBenificiaryRepository  extends CrudRepository<RemittanceBenificiary, Serializable>{

	RemittanceBenificiary findByExRemittancefromBenfi(RemittanceTransaction remittanceTrnxId);
}
