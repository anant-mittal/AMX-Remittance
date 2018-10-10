package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;

public class BeneficiaryAuditEvent extends JaxAuditEvent{

    private static final long serialVersionUID = -5908484246094683956L;
    
     BeneficaryRelationship beneficaryRelationship; 
     RemittancePageDto remittancePageDto;
     BeneficiaryListDTO beneficiaryListDTO;
     BigDecimal customerId;
     BigDecimal placeOrderId;

    public BeneficiaryAuditEvent(Type type, BeneficaryRelationship beneficaryRelationship) {
        super(type);
        this.beneficaryRelationship = beneficaryRelationship;
    }
    
    public BeneficiaryAuditEvent(Type type, RemittancePageDto remittancePageDto) {
        super(type);
        this.remittancePageDto = remittancePageDto;
    }
    
    public BeneficiaryAuditEvent(Type type, BeneficiaryListDTO beneficiaryListDTO) {
        super(type);
        this.beneficiaryListDTO = beneficiaryListDTO;
    }

    public BeneficiaryAuditEvent(Type type,BigDecimal customerId, BigDecimal placeOrderId) {
        super(type);
        this.customerId = customerId;
        this.placeOrderId = placeOrderId;
    }
    
    public BeneficiaryAuditEvent(Type type,BigDecimal customerId) {
        super(type);
        this.customerId = customerId;
     }
   
}
