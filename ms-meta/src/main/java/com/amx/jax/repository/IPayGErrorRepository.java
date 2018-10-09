package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.meta.PaygErrorMaster;

@Transactional
public interface IPayGErrorRepository extends CrudRepository<PaygErrorMaster, BigDecimal>{

	List<PaygErrorMaster> findAll();
}
