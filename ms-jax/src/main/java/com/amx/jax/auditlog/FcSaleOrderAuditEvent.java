package com.amx.jax.auditlog;
/**
 * @Authod :Rabil
 * Date : 04/11/2018
 */

import java.math.BigDecimal;

import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;






public class FcSaleOrderAuditEvent extends JaxAuditEvent{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8858813655272365296L;
	BigDecimal customerId;
    BigDecimal fcSaleOrderId;
    CurrencyMasterDTO currencyMasterListDto;
    PurposeOfTransactionDto purposeOfTrnxDto;
    
    
    
    public FcSaleOrderAuditEvent(Type type, CurrencyMasterDTO currencyMasterListDto) {
        super(type);
        this.currencyMasterListDto = currencyMasterListDto;
    }
    
    
    public FcSaleOrderAuditEvent(Type type, PurposeOfTransactionDto purposeOfTrnxDto) {
        super(type);
        this.purposeOfTrnxDto = purposeOfTrnxDto;
    }
    
	
     public FcSaleOrderAuditEvent(EventType type) {
		super(type);
	}
	
}
