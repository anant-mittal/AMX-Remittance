package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.DeliveryModeDsc;

public interface DeliveryModeDescRepo extends CrudRepository<DeliveryModeDsc, Serializable> {

	List<DeliveryModeDsc> findByLanguageId(BigDecimal languageId);

}
