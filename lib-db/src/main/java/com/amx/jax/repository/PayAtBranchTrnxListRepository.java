package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.PayAtBranchTrnxModel;

public interface PayAtBranchTrnxListRepository extends JpaRepository<PayAtBranchTrnxModel, Serializable>{
	@Query("select w from PayAtBranchTrnxModel w where w.customerId=?1 and (w.applIsActive='Y'and wireTransferStatus='NEW') and wireTransferStatus is not 'PAID' and trunc(w.documentDate)=trunc(sysDate)")
	public List<PayAtBranchTrnxModel> getPbTrnxList(BigDecimal customerId);
	
	@Query("select w from PayAtBranchTrnxModel w where (w.applIsActive='Y'and wireTransferStatus='NEW') and trunc(w.documentDate)=trunc(sysDate) order by documentDate desc")
	public List<PayAtBranchTrnxModel> getPbTrnxListBranch();
}
