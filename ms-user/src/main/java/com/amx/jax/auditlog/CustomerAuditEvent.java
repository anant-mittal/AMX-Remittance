package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.amxlib.model.CustomerModel;

public class CustomerAuditEvent extends JaxAuditEvent {

    private static final long serialVersionUID = 8094510477453442873L;
    
    String identityId;
    BigDecimal customerId;
    CustomerModel customerModel;
    
    public CustomerAuditEvent(Type type, BigDecimal customerId) {
        super(type);
        this.customerId = customerId;
    }
    
    public CustomerAuditEvent(Type type, String identityId) {
        super(type);
        this.identityId = identityId;
    }
    
    public CustomerAuditEvent(Type type, BigDecimal customerId, String identityId) {
        super(type);
        this.customerId = customerId;
        this.identityId = identityId;
    }
    
    public CustomerAuditEvent(Type type, CustomerModel customerModel) {
        super(type);
        this.customerModel = customerModel;
    }

}
