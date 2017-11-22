package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.repository.IViewDistrictDAO;

@Service
public class ViewDistrictService {
	
	@Autowired
	IViewDistrictDAO viewDistrictDao;
	
	public List<ViewDistrict> getDistrict(BigDecimal stateId, BigDecimal districtId, BigDecimal languageId){
		return viewDistrictDao.getDistrict(stateId, districtId, languageId);
	}

}
