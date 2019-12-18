package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForTenant;
import com.amx.jax.pricer.dbmodel.DeliveryModeDesc;
import com.amx.jax.pricer.repository.DeliveryModeDescRepo;

@Component
public class DeliveryModeDescDao {

	@Autowired
	DeliveryModeDescRepo repo;

	@CacheForTenant
	public Map<BigDecimal, DeliveryModeDesc> getByLanguageId(BigDecimal lId) {

		List<DeliveryModeDesc> descriptors = repo.findByLanguageId(lId);

		if (descriptors == null || descriptors.isEmpty()) {
			return new HashMap<BigDecimal, DeliveryModeDesc>();
		}

		return descriptors.stream().collect(Collectors.toMap(d -> d.getDeliveryModeId(), d -> d));
	}

}
