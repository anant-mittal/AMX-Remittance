package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.DeliveryModeDesc;

public interface DeliveryModeDescRepo extends CrudRepository<DeliveryModeDesc, Serializable> {

	List<DeliveryModeDesc> findByLanguageId(BigDecimal languageId);

}
