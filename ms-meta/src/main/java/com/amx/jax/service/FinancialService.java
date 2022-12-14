package com.amx.jax.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.repository.IFinanceYearRespository;
import com.amx.jax.services.AbstractService;

/*
 * Author :Rabil
 */
@Service
public class FinancialService  extends AbstractService{
	
	@Autowired
	IFinanceYearRespository financialYearRepository;

	
	
	
	public AmxApiResponse<UserFinancialYear, Object> getFinancialYear() {
		List<UserFinancialYear> financialList = financialYearRepository.getFinancialYear();
		if(financialList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.buildList(financialList);
	}
	
	
	public UserFinancialYear getUserFinancialYear() {
		List<UserFinancialYear> financialList = financialYearRepository.getFinancialYear();
		return financialList.get(0);
	}
	
	private List<UserFinancialYearDTO> convert(List<UserFinancialYear> financialList){
		List<UserFinancialYearDTO> list = new ArrayList<>();
		for (UserFinancialYear fyr : financialList) {
			UserFinancialYearDTO model = new UserFinancialYearDTO();
			fyr.setFinancialYear(model.getFinancialYear());
			fyr.setFinancialYearID(model.getFinancialYearID());
			fyr.setFullDesc(model.getFullDesc());
			fyr.setShortDesc(model.getShortDesc());
			list.add(model);
		}
		return list;
	}
	

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
