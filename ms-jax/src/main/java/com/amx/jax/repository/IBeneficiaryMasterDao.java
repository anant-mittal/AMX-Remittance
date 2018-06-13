package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.bene.BeneficaryMaster;

public interface IBeneficiaryMasterDao extends DaoRepository<BeneficaryMaster, Serializable>{

	@Query("select bm from BeneficaryMaster bm where bm.beneficaryMasterSeqId=:beneMasterSeqId and bm.isActive='Y'")	
	public List<BeneficaryMaster> getBeneficiaryByBeneMasterId(@Param("beneMasterSeqId") BigDecimal beneMasterSeqId);
	
	public BeneficaryMaster findByBeneficaryMasterSeqId(BigDecimal beneficaryMasterSeqId);
}
