package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.repository.TimezoneRepository;

@Component
public class TimezoneDao {

	@Autowired
	TimezoneRepository timezoneRepository;

	public TimezoneMasterModel findByTimezone(String tz) {

		if (!StringUtils.isEmpty(tz)) {
			return timezoneRepository.findByTimezone(tz);
		}

		return null;
	}

	public TimezoneMasterModel findById(BigDecimal id) {

		if (null == id) {
			return null;
		}

		return timezoneRepository.findOne(id);
	}

}
