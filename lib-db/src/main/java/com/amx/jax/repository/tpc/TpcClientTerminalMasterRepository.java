package com.amx.jax.repository.tpc;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.tpc.TpcClientTerminalMaster;

public interface TpcClientTerminalMasterRepository extends CrudRepository<TpcClientTerminalMaster, Serializable> {

}
