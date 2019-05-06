/**
 * 
 */
package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

/**
 * @author Prashant
 *
 */
public interface IRemittanceApplicationParams {

	BigDecimal getBeneficiaryRelationshipSeqIdBD();

	BigDecimal getLocalAmountBD();

	BigDecimal getForeignAmountBD();

	BigDecimal getCorrespondanceBankIdBD();

	BigDecimal getServiceIndicatorIdBD();

	BigDecimal getDeliveryModeIdBD();

	BigDecimal getRemitModeIdBD();

	Boolean getAvailLoyalityPoints();
	
	//BigDecimal getRoutingCountryIdBD();
	
}
