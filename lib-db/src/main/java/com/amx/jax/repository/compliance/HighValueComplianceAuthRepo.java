package com.amx.jax.repository.compliance;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.compliance.HighValueComplianceAuth;

public interface HighValueComplianceAuthRepo extends CrudRepository<HighValueComplianceAuth, Serializable> {

	List<HighValueComplianceAuth> findAll();

	List<HighValueComplianceAuth> findByhvtLocal(String value);

	List<HighValueComplianceAuth> findByHvtFc(String value);

	List<HighValueComplianceAuth> findBySuspiciousTrnx(String value);
}
