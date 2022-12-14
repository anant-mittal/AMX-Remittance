package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.AdditionalBankDetailData;

public interface AdditionalBankDetailDataRepository extends CrudRepository<AdditionalBankDetailData, Serializable> {

	List<AdditionalBankDetailData> findByBeneAccSeqId(BigDecimal beneAccSeqId);
}
