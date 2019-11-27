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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.remittance.ViewDeliveryMode;
import com.amx.jax.dbmodel.remittance.ViewRemittanceMode;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.GetServiceApplicabilityRequest;
import com.amx.jax.model.response.remittance.GetServiceApplicabilityResponse;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.repository.remittance.IViewDeliveryMode;
import com.amx.jax.repository.remittance.IViewRemittanceMode;
import com.google.common.collect.Sets;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceApplicabilityManager {

	@Autowired
	IServiceApplicabilityRuleDao iServiceApplicabilityRuleDao;
	@Autowired
	MetaData metaData;
	@Autowired
	IViewDeliveryMode deliveryModeRepository;
	@Autowired
	IViewRemittanceMode remittanceModeRepository;

	public List<GetServiceApplicabilityResponse> getServiceApplicability(GetServiceApplicabilityRequest request) {
		if (request.getFieldNames().size() == 0) {
			throw new GlobalException("field name cant be empty");
		}
		List<ServiceApplicabilityRule> rules = iServiceApplicabilityRuleDao.getServiceApplicabilityRules(metaData.getCountryId(),
				BigDecimal.valueOf(request.getCountryId()), BigDecimal.valueOf(request.getCurrencyId()), request.getFieldNames());
		Set<String> availableFields = rules.stream().map(i -> i.getFieldName()).collect(Collectors.toSet());
		Set<String> requestedFields = request.getFieldNames().stream().collect(Collectors.toSet());
		Set<String> outputFields = Sets.intersection(availableFields, requestedFields);

		rules = filterInvalidRemitAndDeliveryModes(rules);

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

	private List<ServiceApplicabilityRule> filterInvalidRemitAndDeliveryModes(List<ServiceApplicabilityRule> rules) {
		Iterable<ViewDeliveryMode> allDeliveryModes = deliveryModeRepository.findAll();
		Iterable<ViewRemittanceMode> allRemittanceModes = remittanceModeRepository.findAll();
		List<BigDecimal> allDeliveryModeIds = new ArrayList<>();
		allDeliveryModes.forEach(i -> allDeliveryModeIds.add(i.getDeliveryModeId()));
		List<BigDecimal> allRemitModeIds = new ArrayList<>();
		allRemittanceModes.forEach(i -> allRemitModeIds.add(i.getRemittanceModeId()));
		return rules.stream().filter(i -> {
			if (!allDeliveryModeIds.contains(i.getDeliveryModeId())) {
				return false;
			}
			if (!allRemitModeIds.contains(i.getRemittanceModeId())) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
	}

}
