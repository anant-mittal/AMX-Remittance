package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.ArcmateScanMaster;

public interface ArcmateScanMasterRepository extends CrudRepository<ArcmateScanMaster, Serializable> {

	List<ArcmateScanMaster> findByModeOfOperationAndScanTypeAndIsActive(String modeOfOperation, String scanType, String isActive);
}
