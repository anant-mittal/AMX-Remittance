package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.GetServiceApplicabilityRequest;
import com.amx.jax.model.response.remittance.GetServiceApplicabilityResponse;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.jax.amxlib.exception.jax.GlobaLException;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceApplicabilityManager {

	@Autowired
	IServiceApplicabilityRuleDao iServiceApplicabilityRuleDao;
	@Autowired
	MetaData metaData;

	public List<GetServiceApplicabilityResponse> getServiceApplicability(GetServiceApplicabilityRequest request) {
		if (request.getFieldNames().size() == 0) {
			throw new GlobaLException("field name cant be empty");
		}
		List<ServiceApplicabilityRule> rules = iServiceApplicabilityRuleDao.getServiceApplicabilityRules(metaData.getCountryId(),
				BigDecimal.valueOf(request.getCountryId()), BigDecimal.valueOf(request.getCurrencyId()), request.getFieldNames());
		Set<GetServiceApplicabilityResponse> ruleSet = rules.stream().map(i -> {
			return new GetServiceApplicabilityResponse(i.getFieldName(), i.getFieldDesc(), ConstantDocument.Yes.equals(i.getMandatory()));
		}).collect(Collectors.toSet());
		return new ArrayList<>(ruleSet);
	}

}
