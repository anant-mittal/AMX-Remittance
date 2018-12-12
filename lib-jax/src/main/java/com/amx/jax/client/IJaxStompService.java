package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;

public interface IJaxStompService extends IJaxService {
    public void publishFxOrderStatusChange(BigDecimal orderNumber, BigDecimal orderYear,  Object updateData);
}