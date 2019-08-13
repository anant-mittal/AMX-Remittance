package com.amx.jax.model.response.remittance.branch;

/**
 *@author :rabil
 *@date   :04 May 2019
 */
import java.util.List;
import java.util.Map;

/**
 * @author rabil
 */
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;

public class DynamicRoutingPricingResponse{
	
	
	
	List<Map<String,List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList;

	public List<Map<String, List<DynamicRoutingPricingDto>>> getDynamicRoutingPricingList() {
		return dynamicRoutingPricingList;
	}

	public void setDynamicRoutingPricingList(List<Map<String, List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList) {
		this.dynamicRoutingPricingList = dynamicRoutingPricingList;
	}
	
}
