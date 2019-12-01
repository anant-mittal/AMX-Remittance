/**
 * 
 */
package com.amx.amxlib.constant;

import java.util.Map;

/**
 * Represents constant parameters used as input or output of db stored
 * procedures
 * 
 * @author Prashant
 *
 */
public enum ApplicationProcedureParam {

	P_CUSTYPE_ID,
	P_SWIFT,
	P_APPLICATION_COUNTRY_ID,
	P_BENEFICIARY_BANK_ID("P_BENE_BANK_ID"),
	P_BENEFICIARY_RELATION_SEQ_ID,
	P_BENEFICIARY_COUNTRY_ID("P_BENE_COUNTRY_ID"),
	P_BENEFICIARY_BRANCH_ID("P_BENE_BANK_BRANCH_ID"),
	P_BENEFICIARY_DISTRICT_ID,
	P_BENEFICIARY_MASTER_ID,
	P_BENEFICIARY_ACCOUNT_NO("P_BENEFICIARY_BANK_ACCOUNT"),
	P_BENEFICIARY_RELASHIONSHIP_ID("P_BENEFICARY_RELATIONSHIP_ID"),
	P_LOCAL_NET_CURRENCY_ID,
	P_DELIVERY_MODE_ID,
	P_REMITTANCE_MODE_ID,
	P_USER_TYPE,
	P_DOCUMENT_ID,
	P_SERVICE_GROUP_CODE,
	P_DOCUMENT_CODE,
	P_USER_FINANCIAL_YEAR,
	P_CUSTOMER_ID,
	P_SERVICE_MASTER_ID,
	P_LC_AMOUNT("P_LOCAL_AMT"),
	P_FC_AMOUNT("P_FOREIGN_TRANX_AMOUNT", "P_FOREIGN_AMT"),
	P_CALCULATED_FC_AMOUNT,
	P_LOCAL_COMMISION_CURRENCY_ID,
	P_ROUTING_BANK_BRANCH_ID,
	P_ROUTING_BANK_ID,
	P_FOREIGN_CURRENCY_ID("P_CURRENCY_ID"),
	P_ROUTING_COUNTRY_ID,
	P_BRANCH_ID,
	P_BENEFICIARY_BANK_COUNTRY_ID("P_BENE_BANK_COUNTRY_ID");

	String[] aliasNames;

	private ApplicationProcedureParam(String... aliasNames) {
		this.aliasNames = aliasNames;
	}

	ApplicationProcedureParam() {
	}

	public void putValue(Map<String, Object> map, Object value) {
		if (this.aliasNames != null && this.aliasNames.length > 0) {
			for (String alias : aliasNames) {
				map.put(alias, value);
			}
		}
		map.put(this.toString(), value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Map<String, Object> map) {
		T value = (T) map.get(this.toString());
		if (value == null) {
			if (this.aliasNames != null && this.aliasNames.length > 0) {
				for (String alias : aliasNames) {
					if (map.get(alias) != null) {
						value = (T) map.get(alias);
						break;
					}
				}
			}
		}
		return value;
	}

}
