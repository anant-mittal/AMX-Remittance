package com.amx.jax.repository.tpc;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.tpc.TpcClientMaster;

public interface TpcClientMasterRepository extends CrudRepository<TpcClientMaster, Serializable> {

	public TpcClientMaster findByClientId(String clientId);
}
