package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;

public interface JaxConditionalFieldRuleRepository extends CrudRepository<JaxConditionalFieldRule, BigDecimal> {

	public List<JaxConditionalFieldRule> findByEntityNameAndConditionKeyAndConditionValue(JaxFieldEntity entity,
			String conditionKey, String conditionValue);
}
