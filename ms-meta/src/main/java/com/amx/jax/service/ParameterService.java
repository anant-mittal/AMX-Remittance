package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.amx.amxlib.constant.AuthType.*;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.AuthenticationViewRepository;
import com.amx.jax.services.AbstractService;

@Service
public class ParameterService extends AbstractService {
	
	
	@Autowired
	AuthenticationLimitCheckDAO authentication;
	
	@Autowired
	AuthenticationViewRepository authenticationViewRepository;
	
	public ApiResponse  getContactUsTime(){
		List<AuthenticationLimitCheckView> contactUsList =authentication.getContactUsTime();
		ApiResponse response = getBlackApiResponse();
		if(contactUsList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(contactUsList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("parameter");
		return response;
	}
	
	
	
	public ApiResponse  getContactPhoneNo(){
		List<AuthenticationLimitCheckView> phoneNoList =authentication.getContactUsPhoneNo();
		ApiResponse response = getBlackApiResponse();
		if(phoneNoList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(phoneNoList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("parameter");
		return response;
	}
	
	
	
	
	
	
	private List<AuthenticationLimitCheckDTO> convert(List<AuthenticationLimitCheckView> checkLimit) {
		List<AuthenticationLimitCheckDTO> list = new ArrayList<>();
		for (AuthenticationLimitCheckView appl : checkLimit) {
			AuthenticationLimitCheckDTO model = new AuthenticationLimitCheckDTO();
			model.setApplicationCountryId(appl.getApplicationCountryId());
			model.setAuthId(appl.getAuthId());
			model.setAuthLimit(appl.getAuthLimit());
			model.setAuthMessage(appl.getAuthMessage());
			model.setAuthorizationType(appl.getAuthorizationType());
			model.setAuthPercentage(appl.getAuthPercentage());
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

	public List<AuthenticationLimitCheckView>  getAllNumberOfTxnLimits(){
		List<AuthenticationLimitCheckView> authLimits =authentication.getAllNumberOfTxnLimits();
		return authLimits;
	}

	public AuthenticationLimitCheckView getOnlineTxnLimit() {
		AuthenticationLimitCheckView authLimits = authentication.getTop1OnlineTxnLimit();
		return authLimits;
	}
	
	public AuthenticationLimitCheckView getAuthenticationViewRepository(String authType) {
		return authentication.findByAuthorizationType(authType);
	}
	
	public AuthenticationLimitCheckView getPerCustomerPerBeneTrnxLimit() {
		AuthenticationLimitCheckView authLimits = authentication.getPerBeneTxnLimit();
		return authLimits;
	}

	public ApiResponse getJaxMetaParameter() {
		List<AuthenticationLimitCheckView> allAuthLimits = authentication.findAll();
		Map<String, BigDecimal> authMap = allAuthLimits.stream()
				.collect(Collectors.toMap(x -> x.getAuthorizationType(), x -> x.getAuthLimit()));
		JaxMetaParameter metaParams = new JaxMetaParameter();
		metaParams.setNewBeneTransactionTimeLimit(authMap.get(NEW_BENE_TRANSACT_TIME_LIMIT.getAuthType()));
		metaParams.setMaxDomAmountLimit((authMap.get(MAX_DOM_AMOUNT_LIMIT.getAuthType())));
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(metaParams);
		response.getData().setType("jaxmetaparameter");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

}
