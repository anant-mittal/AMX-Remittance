package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.google.common.collect.Sets;
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
		Set<String> availableFields = rules.stream().map(i -> i.getFieldName()).collect(Collectors.toSet());
		Set<String> requestedFields = request.getFieldNames().stream().collect(Collectors.toSet());
		Set<String> outputFields = Sets.intersection(availableFields, requestedFields);

		List<GetServiceApplicabilityResponse> serviceApplicabilityList = new ArrayList<>();
		Map<String, GetServiceApplicabilityResponse> fieldMap = new HashMap<>();
		outputFields.stream().forEach(i -> {
			GetServiceApplicabilityResponse fieldServiceApplicability = new GetServiceApplicabilityResponse();
			fieldServiceApplicability.setFieldName(i);
			serviceApplicabilityList.add(fieldServiceApplicability);
			fieldMap.put(i, fieldServiceApplicability);
		});

		rules.stream().forEach(i -> {
			if (ConstantDocument.Yes.equals(i.getMandatory())) {
				GetServiceApplicabilityResponse getServiceApplicabilityResponse = fieldMap.get(i.getFieldName());
				if (getServiceApplicabilityResponse != null) {
					getServiceApplicabilityResponse.setMandatory(true);
				}
			}
		});
		return serviceApplicabilityList;
	}

}
