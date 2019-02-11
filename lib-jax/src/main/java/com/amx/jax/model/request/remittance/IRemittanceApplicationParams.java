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

	Long getBeneficiaryRelationshipSeqId();

	BigDecimal getLocalAmount();

	BigDecimal getForeignAmount();

	Long getCorrespondanceBankId();

	Long getServiceIndicatorId();

}
