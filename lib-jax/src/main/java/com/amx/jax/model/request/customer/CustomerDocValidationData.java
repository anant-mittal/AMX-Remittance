package com.amx.jax.model.request.customer;

import java.math.BigDecimal;

/**
 * Represents data needed to valiation docs uploaded by customer
 * 
 * @author prashant
 *
 */
public interface CustomerDocValidationData {

	String getEmployer();

	BigDecimal getIncomeRangeId();

	BigDecimal getArticleDetailsId();
	
	Boolean isCreateCustomerRequest();

	String getIdentityInt();

	BigDecimal getIdentityTypeId();

}
