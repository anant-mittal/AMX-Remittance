package com.amx.jax.service;

import static com.amx.amxlib.constant.AuthType.MAX_DOM_AMOUNT_LIMIT;
import static com.amx.amxlib.constant.AuthType.NEW_BENE_TRANSACT_TIME_LIMIT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.TransactionLimitCheckView;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.AuthenticationViewRepository;
import com.amx.jax.repository.TransactionLimitCheckDAO;
import com.amx.jax.services.AbstractService;

@Service
public class ParameterService extends AbstractService {
	
	
	@Autowired
	AuthenticationLimitCheckDAO authentication;
	
	@Autowired
	TransactionLimitCheckDAO transactionLimit;
	
	@Autowired
	AuthenticationViewRepository authenticationViewRepository;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	private MetaData metaData;
	
	public AmxApiResponse<AuthenticationLimitCheckDTO, Object>  getContactUsTime(){
		List<AuthenticationLimitCheckView> contactUsList =authentication.getContactUsTime();
		if(contactUsList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.buildList(convert(contactUsList));
	}
	
	
	
	public AmxApiResponse<AuthenticationLimitCheckDTO, Object>  getContactPhoneNo(){
		List<AuthenticationLimitCheckView> phoneNoList =authentication.getContactUsPhoneNo();
		if(phoneNoList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.buildList(convert(phoneNoList));
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
			model.setAuthDesc(appl.getAuthDesc());
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

	public AmxApiResponse<JaxMetaParameter, Object> getJaxMetaParameter() {
		List<AuthenticationLimitCheckView> allAuthLimits = authentication.findAll();
		Map<String, BigDecimal> authMap = allAuthLimits.stream()
				.filter(x -> (x.getAuthorizationType() != null && x.getAuthLimit() != null))
				.collect(Collectors.toMap(x -> x.getAuthorizationType(), x -> x.getAuthLimit(), (v1, v2) -> {
					return v1;
				}));
		JaxMetaParameter metaParams = new JaxMetaParameter();
		metaParams.setNewBeneTransactionTimeLimit(authMap.get(NEW_BENE_TRANSACT_TIME_LIMIT.getAuthType()));
		metaParams.setMaxDomAmountLimit((authMap.get(MAX_DOM_AMOUNT_LIMIT.getAuthType())));
		ViewCompanyDetails company = companyService.getCompanyDetail(metaData.getLanguageId());
		metaParams.setApplicationCountryId(company.getApplicationCountryId());
		return AmxApiResponse.build(metaParams);
	}
	
	public List<TransactionLimitCheckView>  getAllTxnLimits(){
		List<TransactionLimitCheckView> trnxLimits =transactionLimit.findAll();
		return trnxLimits;
	}

}
