package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.ScanIdTypeMaster;

public interface ScanIdTypeMasterRepository extends CrudRepository<ScanIdTypeMaster, Serializable> {

	List<ScanIdTypeMaster> findByidTypeIdAndScanIndAndIsActive(BigDecimal idTypeId, String scanInd, String isActive);
}
