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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicRoutingPricingResponse {

	public static enum SELECTION {
		MAX_RATE, MIN_RATE, MIN_TIME, MAX_TIME
	}

	private SELECTION selection = SELECTION.MIN_TIME;

	List<Map<String, List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList;

	public List<Map<String, List<DynamicRoutingPricingDto>>> getDynamicRoutingPricingList() {
		return dynamicRoutingPricingList;
	}

	public void setDynamicRoutingPricingList(
			List<Map<String, List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList) {
		this.dynamicRoutingPricingList = dynamicRoutingPricingList;
	}

	public SELECTION getSelection() {
		return selection;
	}

	public void setSelection(SELECTION selection) {
		this.selection = selection;
	}

}
