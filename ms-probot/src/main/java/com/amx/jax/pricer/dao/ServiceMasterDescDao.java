package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.repository.ServiceMasterDescRepository;

@Component
public class ServiceMasterDescDao {

	@Autowired
	ServiceMasterDescRepository serviceMasterDescRepository;

	public ServiceMasterDesc getServiceById(BigDecimal serviceId) {
		return serviceMasterDescRepository.getServiceById(serviceId);
	}

	public Map<BigDecimal, ServiceMasterDesc> getByServiceIdIn(List<BigDecimal> serviceIds) {
		List<ServiceMasterDesc> sList = serviceMasterDescRepository.findByLanguageIdAndServiceIdIn(BigDecimal.ONE,
				serviceIds);

		Map<BigDecimal, ServiceMasterDesc> sMap;

		if (sList != null && !sList.isEmpty()) {
			sMap = sList.stream().collect(Collectors.toMap(s -> s.getServiceId(), s -> s));
		} else {
			sMap = new HashMap<BigDecimal, ServiceMasterDesc>();
		}

		return sMap;

	}

}
