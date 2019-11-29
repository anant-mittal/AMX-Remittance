package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.bene.BeneficaryStatus;

public interface BeneficaryStatusRepository extends CrudRepository<BeneficaryStatus, BigDecimal> {

	public BeneficaryStatus findByBeneficaryStatusName(String beneficaryStatusName);

	public List<BeneficaryStatus> findByIsActive(String isActive);

	@Query(value = "SELECT * FROM EX_BENEFICARY_STATUS_MASTER  WHERE NVL(SERVICE_GROUP_CODE,' ') <>  (?2) AND ISACTIVE=?1", nativeQuery = true)
	public List<BeneficaryStatus> findByServiceGroupAndActive(String isActive, String serviceGroupCode);
}
