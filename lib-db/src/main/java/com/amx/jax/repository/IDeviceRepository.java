package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dict.UserClient.ClientType;

public interface IDeviceRepository  extends CrudRepository<Device, Serializable>{
	
	public Device findByDeviceTypeAndBranchSystemInventoryId(ClientType deviceType,BigDecimal branchSystemInventoryId);

}
