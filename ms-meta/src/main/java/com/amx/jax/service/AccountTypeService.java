package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class AccountTypeService extends AbstractService {
	private Logger logger = Logger.getLogger(AccountTypeService.class);
	
	@Autowired
	IAccountTypeFromViewDao accountTypeFromViewDao;
	
	
	public ApiResponse getAccountTypeFromView(BigDecimal countryId) {
		ApiResponse response = getBlackApiResponse();
		List<AccountTypeFromViewModel> accountTypeList = accountTypeFromViewDao.getAccountTypeByCountryId(countryId);
			if (accountTypeList.isEmpty()) {
				throw new GlobalException("Account type not found");
			} else {
				response.getData().getValues().addAll(convert(accountTypeList));
				response.setResponseStatus(ResponseStatus.OK);
			}
			response.getData().setType("accountType");
			return response;
		
		
	}
	
	private List<AccountTypeDto> convert(List<AccountTypeFromViewModel> accountTypeList) {
		List<AccountTypeDto> output = new ArrayList<>();
		accountTypeList.forEach(accountType -> output.add(convert(accountType)));
		return output;
	}

	private AccountTypeDto convert(AccountTypeFromViewModel accountTypeFromViewModel) {
		AccountTypeDto dto = new AccountTypeDto();
		try {
			BeanUtils.copyProperties(dto, accountTypeFromViewModel);
			dto.setBankAccountTypeId(accountTypeFromViewModel.getAdditionalAmiecId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.debug("unable to convert currency", e);
		}
		return dto;
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
